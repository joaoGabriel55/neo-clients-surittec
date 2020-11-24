package com.crud.neoclients.api.services;

import br.com.caelum.stella.ValidationMessage;
import br.com.caelum.stella.validation.CPFValidator;
import com.crud.neoclients.api.models.Cliente;
import com.crud.neoclients.api.models.Email;
import com.crud.neoclients.api.models.Telefone;
import com.crud.neoclients.api.models.enums.TipoTelefone;
import com.crud.neoclients.api.repositories.ClienteRepository;
import com.crud.neoclients.api.services.interfaces.IClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.MaskFormatter;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClienteService implements IClienteService {

    @Autowired
    private ClienteRepository repository;

    @Override
    public Cliente saveOrUpdate(Cliente cliente) throws Exception {
        try {
            isAllTelefonesValid(cliente.getTelefones());
            isAllEmailsValid(cliente.getEmails());

            validateCpf(cliente.getCpf());
            cliente.setCpf(cliente.getCpf().replaceAll("\\D", ""));
            if (cliente.getEndereco() != null)
                cliente.getEndereco().setCep(cliente.getEndereco().getCep().replaceAll("\\D", ""));

            cliente.getEndereco().setCliente(cliente);
            cliente.getTelefones().forEach(telefone -> telefone.setCliente(cliente));
            cliente.getEmails().forEach(email -> email.setCliente(cliente));
            Cliente result = this.repository.save(cliente);
            return this.getClienteFormatted(result);
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        } catch (NullPointerException e) {
            e.printStackTrace();
            throw new NullPointerException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public Cliente findById(Long id) throws Exception {
        try {
            Optional<Cliente> result = this.repository.findById(id);
            if (!result.isPresent())
                return null;
            return this.getClienteFormatted(result.get());
        } catch (ParseException e) {
            throw new ParseException(e.getMessage(), e.getErrorOffset());
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public List<Cliente> findAll() throws Exception {
        try {
            return this.repository.findAll().stream().map(cliente -> {
                        try {
                            return this.getClienteFormatted(cliente);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return cliente;
                    }
            ).collect(Collectors.toList());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public void delete(Long id) throws Exception {
        try {
            Optional<Cliente> result = this.repository.findById(id);
            if (!result.isPresent())
                throw new NullPointerException();

            this.repository.delete(result.get());
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void validateCpf(String cpf) throws Exception {
        CPFValidator cpfValidator = new CPFValidator();
        List<ValidationMessage> errors = cpfValidator.invalidMessagesFor(cpf);
        if (errors.size() > 0)
            throw new Exception("CPF " + cpf + " é inválido");
    }

    private void isAllTelefonesValid(Set<Telefone> telefones) throws Exception {
        for (Telefone telefone : telefones) {
            telefone.setNumero(telefone.getNumero().replaceAll("\\D", ""));
            if (telefone.getTipoTelefone() == TipoTelefone.CELULAR) {
                if (telefone.getNumero().length() != 11)
                    throw new Exception("Telefone tipo CELULAR: " + telefone.getNumero() + " é inválido");
            } else {
                if (telefone.getNumero().length() != 10) {
                    throw new Exception("Telefone tipo " +
                            telefone.getTipoTelefone().name() + ": " +
                            telefone.getNumero() + " é inválido"
                    );
                }
            }
        }
    }

    private void isAllEmailsValid(Set<Email> emails) throws Exception {
        String regex = "^(.+)@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        for (Email email : emails) {
            Matcher matcher = pattern.matcher(email.getEmailAddress());
            if (!matcher.matches())
                throw new Exception("Email: " + email.getEmailAddress() + " é inválido");

        }
    }

    private Cliente getClienteFormatted(Cliente cliente) throws ParseException {
        MaskFormatter cpfMask = new MaskFormatter("###.###.###-##");
        cpfMask.setValueContainsLiteralCharacters(false);
        cliente.setCpf(cpfMask.valueToString(cliente.getCpf()));

        MaskFormatter cepMask = new MaskFormatter("#####-###");
        cepMask.setValueContainsLiteralCharacters(false);
        cliente.getEndereco().setCliente(null);
        cliente.getEndereco().setCep(cepMask.valueToString(cliente.getEndereco().getCep()));

        cliente.getEmails().forEach(email -> email.setCliente(null));

        for (Telefone telefone : cliente.getTelefones()) {
            telefone.setCliente(null);
            String number = telefone.getNumero();
            String maskPattern = "(##) ####-####";
            if (telefone.getTipoTelefone() == TipoTelefone.CELULAR) {
                maskPattern = "(##) #####-####";
                MaskFormatter phoneMask = new MaskFormatter(maskPattern);
                phoneMask.setValueContainsLiteralCharacters(false);
                telefone.setNumero(phoneMask.valueToString(number));
            } else {
                MaskFormatter phoneMask = new MaskFormatter(maskPattern);
                phoneMask.setValueContainsLiteralCharacters(false);
                telefone.setNumero(phoneMask.valueToString(number));
            }
        }
        return cliente;
    }
}
