/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import java.io.File;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JOptionPane;

/**
 *
 * @author informatica01
 */
public class ControlEmail {

    public void EmailAnexo(model.modelEmail mod, File anexo[]) {
        try {
            //usuario e senha do seu gmail
            final String usuario = mod.getRemetente();
            final String senha = mod.getSenha();
            //config. do gmail
            Properties mailProps = new Properties();
            mailProps.put("mail.transport.protocol", "smtp");
            mailProps.put("mail.smtp.starttls.enable", "true");
            mailProps.put("mail.smtp.host", mod.getSmtp());
            mailProps.put("mail.smtp.auth", "true");
            mailProps.put("mail.smtp.user", usuario);
            mailProps.put("mail.debug", "true");
            mailProps.put("mail.smtp.port", mod.getPorta());
            mailProps.put("mail.smtp.socketFactory.port", mod.getPorta());
            mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            mailProps.put("mail.smtp.socketFactory.fallback", "false");
            //eh necessario autenticar
            Session mailSession = Session.getInstance(mailProps, new Authenticator() {

                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(usuario, senha);
                }
            });
            mailSession.setDebug(false);
            //config. da mensagem
            Message mailMessage = new MimeMessage(mailSession);
            //remetente
            mailMessage.setFrom(new InternetAddress(mod.getRemetente(), mod.getRemetente()));
            //destinatario
            mailMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mod.getDestinatario()));
            //mensagem que vai no corpo do email
            MimeBodyPart mbpMensagem = new MimeBodyPart();
            mbpMensagem.setText(mod.getMensagem());
            //partes do email
            Multipart mp = new MimeMultipart();
            mp.addBodyPart(mbpMensagem);
            String Endereco_Anexo = "";
            if (anexo != null) { // se tiver alguma coisa anexada ela inicializar o comando abaixo
                for (File element : anexo) {

                    Endereco_Anexo = element.getPath();
                    String imagem = Endereco_Anexo;
                    File Arquivo = new File(imagem);
                    //setando o anexo
                    MimeBodyPart mbpAnexo = new MimeBodyPart();
                    mbpAnexo.setDataHandler(new DataHandler(new FileDataSource(Arquivo)));
                    mbpAnexo.setFileName(Arquivo.getName());
                    mp.addBodyPart(mbpAnexo);
                }
            }
            //assunto do email
            mailMessage.setSubject(mod.getAssunto());
            //seleciona o conteudo 
            mailMessage.setContent(mp);
            //envia o email
            Transport.send(mailMessage);
            JOptionPane.showMessageDialog(null, "Email Enviado com Sucesso");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao enviar email !!!!" + e);
        }
    }
}
