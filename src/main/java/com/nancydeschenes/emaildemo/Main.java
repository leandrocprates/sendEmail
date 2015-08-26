package com.nancydeschenes.emaildemo;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.smtp.SMTPMessage;
import gui.ava.html.image.generator.HtmlImageGenerator;

/**
 * REference : 
 * 
 * http://blog.smartbear.com/how-to/how-to-send-email-with-embedded-images-using-java/
 * 
 * https://github.com/nancyd/emaildemo
 * 
 * 
**/



public class Main {

  public static void main(String[] args) throws MessagingException, IOException {
      
    Session session = buildGoogleSession();
    Message simpleMessage = buildSimpleMessage(session);
    sendMessageToAddress(simpleMessage, "leandro.prates@asisprojetos.com.br");
    
    Message messageWithAttachment = buildMessageWithAttachment(session);
    sendMessageToAddress(messageWithAttachment, "leandro.prates@asisprojetos.com.br");
    
    Message withImage = buildMessageWithEmbeddedImage(session);
    addressAndSendMessage(withImage, "leandro.prates@asisprojetos.com.br");
    

    Message withProgressBar = buildHTML(session);
    addressAndSendMessage(withProgressBar, "leandro.prates@asisprojetos.com.br");
    
    Message withProgressBar2 = buildHTML2(session);
    addressAndSendMessage(withProgressBar2, "leandro.prates@asisprojetos.com.br");//leandro.prates@hotmail.com.br
    

    Message htmlTeste = HtmlTeste(session);
    addressAndSendMessage(htmlTeste, "leandro.prates@asisprojetos.com.br");
    
    Message htmlTeste2 = HtmlTeste2(session);
    addressAndSendMessage(htmlTeste2, "leandro.prates@asisprojetos.com.br");
    
    Message htmlTeste3 = HtmlTeste3(session);
    addressAndSendMessage(htmlTeste3, "leandro.prates@asisprojetos.com.br");
    
    Message htmlTeste4 = HtmlTeste4(session);
    addressAndSendMessage(htmlTeste4, "leandro.prates@asisprojetos.com.br");
    

  }

  //
  //
  // configuration methods
  //
  //

  /**
   * Build a Session object for an SMTP server that requires neither TSL or
   * authentication
   * 
   * @return a Session for sending email
   */
  public static Session buildSimpleSession() {
      
    Properties mailProps = new Properties();
    mailProps.put("mail.transport.protocol", "smtp");
    mailProps.put("mail.host", "localhost");
    mailProps.put("mail.from", "example@example.com");
    return Session.getDefaultInstance(mailProps);
    
  }

  /**
   * Build a Session object for an SMTP server that requires both TSL and
   * authentication. This uses Gmail as an example of such a server
   * 
   * @return a Session for sending email
   */
  public static Session buildGoogleSession() {
      
    Properties mailProps = new Properties();
    mailProps.put("mail.transport.protocol", "smtp");
    mailProps.put("mail.host", "smtp.gmail.com");
    mailProps.put("mail.from", "leandro.prates@gmail.com");
    mailProps.put("mail.smtp.starttls.enable", "true");
    mailProps.put("mail.smtp.port", "587");
    mailProps.put("mail.smtp.auth", "true");
    // final, because we're using it in the closure below
    final PasswordAuthentication usernamePassword = new PasswordAuthentication(
        "leandro.prates@gmail.com", "senha");
    
    Authenticator auth = new Authenticator() {
      protected PasswordAuthentication getPasswordAuthentication() {
        return usernamePassword;
      }
    };
    
    Session session = Session.getInstance(mailProps, auth);
    session.setDebug(true);
    
    return session;

  }

  
  //
  //
  // Message building methods
  //
  //
  
  /**
   * Build a simple text message - no attachments.
   * 
   * @param session
   * @return a multipart MIME message with only one part, a simple text message.
   * @throws MessagingException
   */
  public static Message buildSimpleMessage(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText("Hello there! This is simple demo message");
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("Demo message");
    
    return m;
    
  }

  
  /**
   * Build a text message with an image as an attachment.  
   * 
   * @param session
   * @return a multipart MIME message where the first part is a text 
   * message and the second part is an image
   * @throws MessagingException
   * @throws IOException
   */
  public static Message buildMessageWithAttachment(Session session)
      throws MessagingException, IOException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();

    // The main (text) part
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText("Hello there! This is simple demo message");
    content.addBodyPart(mainPart);
    
    // The image
    MimeBodyPart imagePart = new MimeBodyPart();
    imagePart.attachFile("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/teapot.jpg");
    content.addBodyPart(imagePart);
    
    m.setContent(content);
    m.setSubject("Demo message with a teapot!");
    return m;
    
  }

  /**
   * Build an HTML message with an image embedded in the message.
   * 
   * @param session
   * @return a multipart MIME message where the main part is an HTML message and the 
   * second part is an image that will be displayed within the HTML.
   * @throws MessagingException
   * @throws IOException
   */
  public static Message buildMessageWithEmbeddedImage(Session session)
      throws MessagingException, IOException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart("related");
    
    // ContentID is used by both parts
    String cid = ContentIdGenerator.getContentId();
    String cid2 = ContentIdGenerator.getContentId();
    
    // HTML part
    MimeBodyPart textPart = new MimeBodyPart();
    textPart.setText("<html><head>"
      + "<title>This is not usually displayed</title>"
      + "</head>\n"
      + "<body><div><b>Hi there!</b></div>"
      + "<div>Sending HTML in email is so <i>cool!</i> </div>\n"
      + "<div>And here's an image: <img src=\"cid:"
      + cid
      + "\" /></div>\n" + "<div>I hope you like it!</div>"
            
      + "<div>The second  image is : <img src=\"cid:"
      + cid2
      + "\" /></div>\n"
            
        +"</body></html>", 
      "US-ASCII", "html");
    content.addBodyPart(textPart);

    // Image part
    MimeBodyPart imagePart = new MimeBodyPart();
    imagePart.attachFile("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/teapot.jpg");
    imagePart.setContentID("<" + cid + ">");
    imagePart.setDisposition(MimeBodyPart.INLINE);
    content.addBodyPart(imagePart);

    
    //Second Image part
    MimeBodyPart imagePart2 = new MimeBodyPart();
    imagePart2.attachFile("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/linux.jpg");
    imagePart2.setContentID("<" + cid2 + ">");
    imagePart2.setDisposition(MimeBodyPart.INLINE);
    content.addBodyPart(imagePart2);
    
    
    m.setContent(content);
    m.setSubject("Demo HTML message");
    return m;
    
  }

  

  
    public static Message buildHTML(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    
            "<!DOCTYPE html>"+
          "<html>"+
          "<head>"+
          "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">"+
                    
          "<style>"+ 
                    "h1{color:blue;}"+
          "</style>"+          
                    
          "</head>"+
          "<body>"+
          "<h1>My First CSS Example</h1>"+
          "<p>This is a paragraph.</p>"+

          "Downloading progress  BAR::"+
          "<progress value=\"22\" max=\"100\">"+
          "</progress>"+
          "Fim barra de progresso"+          

          "<br/><br/>"+

          "<div class=\"progress\">"+
            "<div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"60\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 60%;\">"+
            "  60%"+
            "</div>"+
          "</div>"+

          "</body>"+
          "</html>" , "US-ASCII", "html"

    );
    
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("SendHTML ProgressBar");
    
    
    return m;
    
  }
    
    
    
    public static Message buildHTML2(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    
          
"<!DOCTYPE html>"+
"<html lang=\"en\">"+
    "<head>"+
        "<meta charset=\"utf-8\">"+
        "<title>Simple HTML+CSS progress bar</title>"+
    "</head>"+
    "<body>"+

        "<style>"+
            "body {"+
                
                "font-family: Arial, sans-serif;"+
                "padding: 0 10%;"+
                "background-color: #eaeaea;"+
            "}"+
            ".graph {"+
                "width: 500px; "+
                "height: 30px;"+
                "border: 1px solid #888; "+
                "background: rgb(168,168,168);"+
                "background: -moz-linear-gradient(top, rgba(168,168,168,1) 0%, rgba(204,204,204,1) 23%);"+
                "background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(168,168,168,1)), color-stop(23%,rgba(204,204,204,1)));"+
                "background: -webkit-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "background: -o-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "background: -ms-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a8a8a8', endColorstr='#cccccc',GradientType=0 );"+
                "background: linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "position: relative;"+
            "}"+
            "#bar {"+
                "height: 29px; "+
                "background: rgb(255,197,120); "+
                "background: -moz-linear-gradient(top, rgba(255,197,120,1) 0%, rgba(244,128,38,1) 100%); "+
                "background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(255,197,120,1)), color-stop(100%,rgba(244,128,38,1))); "+
                "background: -webkit-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: -o-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: -ms-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "border-top: 1px solid #fceabb;"+
            "}"+
            "#bar p { position: absolute; text-align: center; width: 100%; margin: 0; line-height: 30px; }"+
            ".error {"+
                "background-color: #fceabb;"+
                "padding: 1em;"+
                "font-weight: bold;"+
                "color: red;"+
                "border: 1px solid red;"+
            "}"+
        "</style>"+

        "<h2>Resumo de Consumo</h2>"+

	"SpedFiscal:"+
        "<div id=\"progress\" class=\"graph\"><div id=\"bar\" style=\"width:98%\"><p>10 de 300</p></div></div>"+

	"<br/>"+

        
	"EFD:"+
        "<div   style=\"width: 300px; "+
                "height: 30px;"+
                "border: 1px solid #888; "+
                "background: rgb(168,168,168);"+
                "position: relative;\"><div  style=\" width:10%;height: 29px; "+
                "background: rgb(255,197,120); "+
                "border-top: 1px solid #fceabb;\"><p style=\"position: absolute; text-align: center; width: 100%; margin: 0; line-height: 30px;\">15</p></div></div>"+
        
        "<br/>"+
        "<br/>"+
        "<br/>"+
        
        
        "SPED: " + 
        " <div style=\"width: 500px; " +
        " border: 1px solid black;" +
        " position: relative;" +
        " padding: 3px;\">"+
            "<span id=\"percent\" style=\"position: absolute;left: 50%;display:inline-block;text-align: center\" >30%</span>"+
            "<div id=\"bar\" style=\"height: 20px; background-color: blue; width: 30%;display:inline-block\" ></div>"+
        "</div>"+
        

        
        
    "</body>"+
"</html>"
            
           , "US-ASCII", "html"

    );
    
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("buildHTML2");
    
    
    
    gerarImagem();
    return m;
    
  }
    
    
  public static void gerarImagem(){
      
    HtmlImageGenerator imageGenerator = new HtmlImageGenerator();
    imageGenerator.loadHtml("<b>Hello World!</b> Please goto <a title=\"Goto Google\" href=\"http://www.google.com\">Google</a>.");
    imageGenerator.saveAsImage("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/hello-world.png");
    
    
    imageGenerator.loadHtml(
    
"<!DOCTYPE HTML PUBLIC \"-W3CDTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">"+
"<html lang=\"en\">"+
    "<head>"+
        "<meta charset=\"utf-8\">"+
        "<title>Simple HTML+CSS progress bar</title>"+
    "</head>"+
    "<body>"+
        "<h2>Resumo de Consumo</h2>"+
	"<br/>"+
	"EFD:"+
        "Teste  "+
	"<div style=\"width: 500px; border: 1px solid black; position: relative; padding: 3px;\">"+
    	"<span id=\"percent\" style=\"position: absolute;left: 50%;text-align: center\" >30%</span>"+
    	"<div id=\"bar\" style=\"height: 20px; background-color: blue; width: 70%;\"  ></div></div>"+
    "</body>"+
"</html>"
    
    );
    
    imageGenerator.saveAsImage("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/hello-world2.png");
    

    
    imageGenerator.loadHtml(
    
        "<h2>Resumo de Consumo</h2>"+
	"<br/>"+
	"EFD:"+
        "Teste  "+
	"<div style=\"width: 500px; border: 1px solid black; position: relative; padding: 3px;display:inline-block\">"+
    	"<span id=\"percent\" style=\"position: absolute;left: 50%;text-align: center\" >30%</span>"+
    	"<div id=\"bar\" style=\"height: 20px; background-color: blue; width: 70%;\"  ></div></div>"
    
    );
    
    imageGenerator.saveAsImage("C:/Users/lprates/Documents/NetBeansProjects/SendEmail/src/main/resources/hello-world3.png");
    
    
    
  } 

  
    
    

  public static Message HtmlTeste(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    
"<!DOCTYPE html>"+
"<html lang=\"en\">"+
  "<head>"+
    "<meta charset=\"utf-8\">"+
    "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">"+
    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"+
    "<title>Bootstrap 101 Template</title>"+
    "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css\">"+
  "</head>"+

  "<body>"+
    "<h1>Hello, world!</h1>"+
	"<div class=\"progress\">"+
	  "<div class=\"progress-bar\" role=\"progressbar\" aria-valuenow=\"60\" aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width: 60%;\">"+
	   " 60% "+
	  "</div>"+
	"</div>"+
  "</body>"+
"</html>    " , "US-ASCII", "html"
    
    
    );
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("HtmlTeste");
    
    return m;
    
  }
  
  
  public static Message HtmlTeste2(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    
"<!DOCTYPE html>"+
"<html>"+
  "<head>"+
    "<title>Foundation | Welcome</title>"+
  "</head>"+
    "<body>"+
        "<style>"+
        
   ".progress {"+
     "width: 30%;	 "+
     "background-color: #F6F6F6;"+
     "height: 1.5625rem;"+
     "border: 1px solid white;"+
     "padding: 0.125rem;"+
     "margin-bottom: 0.625rem; }"+
     ".progress .meter {"+
       "background: #2ba6cb;"+
       "height: 100%;"+
       "display: block; }"+
     ".progress.secondary .meter {"+
       "background: #e9e9e9;"+
       "height: 100%;"+
       "display: block; }"+
     ".progress.success .meter {"+
       "background: #5da423;"+
       "height: 100%;"+
       "display: block; }"+
     ".progress.alert .meter {"+
       "background: #c60f13;"+
       "height: 100%;"+
       "display: block; }"+
     ".progress.radius {"+
       "border-radius: 3px; }"+
       ".progress.radius .meter {"+
         "border-radius: 2px; }"+
     ".progress.round {"+
       "border-radius: 1000px; }"+
       ".progress.round .meter {"+
         "border-radius: 999px; }"+
        
        "p.intro{"+
        "color: green;"+
        "}"+  
        
        "</style>"+
        
        "<div class=\"round progress large-10\">"+
          "<span class=\"meter\" style=\"width: 10%\"></span>"+
        "</div>"+

        "<p class=\"intro\">Oi tudo bem</p>"+
        
        "</body>"+
      "</html>"  , "US-ASCII", "html"
            
    );
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("HtmlTeste2");
    
    return m;
    
  }

  
  public static Message HtmlTeste3(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    

    "<!DOCTYPE html>"+
"<html lang=\"en\">"+
  "  <head>"+
   "     <meta charset=\"utf-8\">"+

    "    <title>Simple HTML+CSS progress bar</title>"+
     "   <style>"+
      "      body {"+
               
       "         font-family: Arial, sans-serif;"+
       "         padding: 0 10%;"+
        "        background-color: #eaeaea;"+
       "     }"+
        "    .graph {"+
         "       width: 500px; "+
          "      height: 30px;"+
          "      border: 1px solid #888; "+
          "      background: rgb(168,168,168);"+
          "      background: -moz-linear-gradient(top, rgba(168,168,168,1) 0%, rgba(204,204,204,1) 23%);"+
          "      background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(168,168,168,1)), color-stop(23%,rgba(204,204,204,1)));"+
          "      background: -webkit-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
          "      background: -o-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
          "      background: -ms-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
          "      filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a8a8a8', endColorstr='#cccccc',GradientType=0 );"+
          "      background: linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
          "      position: relative;"+
          "  }"+
          "  #bar {"+
          "      height: 29px; "+
          "      background: rgb(255,197,120); "+
          "      background: -moz-linear-gradient(top, rgba(255,197,120,1) 0%, rgba(244,128,38,1) 100%); "+
          "      background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(255,197,120,1)), color-stop(100%,rgba(244,128,38,1))); "+
          "      background: -webkit-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
          "      background: -o-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
          "      background: -ms-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
          "      background: linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
          "      border-top: 1px solid #fceabb;"+
          "  }"+
          "  #bar p { position: absolute; text-align: center; width: 100%; margin: 0; line-height: 30px; }"+
          "  .error {"+
          
          "      background-color: #fceabb;"+
          "      padding: 1em;"+
          "      font-weight: bold;"+
          "      color: red;"+
          "      border: 1px solid red;"+
          "  }"+
        "</style>"+

    "</head>"+
    "<body>"+
    "    <h1>Simple HTML+CSS progress bar</h1>"+
    "    <p>You could animate this, but it was intended as a progress indicator through a multi-page form process.</p>"+
    "    <h2>HTML5 <code>progress</code> element with HTML/CSS fallback</h2>"+
    "       <progress value=\"34\" max=\"100\"><div id=\"progress\" class=\"graph\"><div id=\"bar\" style=\"width:34%\"><p>34% complete</p></div></div></progress>"+
    "       <p><em>(Special thanks to Krijn Hoetmer)</em></p>"+
    "    <h2>Just the HTML/CSS fallback (a <code>div</code> containing another <code>div</code>)</h2>"+
    "    <div id=\"progress\" class=\"graph\"><div id=\"bar\" style=\"width:60%\"><p>60% complete</p></div></div>"+
    "</body>"+
"</html>        " , "US-ASCII", "html"
            
            
    );
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("HtmlTeste3");
    
    return m;
    
  }
  
  public static Message HtmlTeste4(Session session)
      throws MessagingException {
      
    SMTPMessage m = new SMTPMessage(session);
    MimeMultipart content = new MimeMultipart();
    MimeBodyPart mainPart = new MimeBodyPart();
    mainPart.setText(
    
"<!DOCTYPE html>"+
"<html lang=\"en\">"+
    "<head>"+
        "<meta charset=\"utf-8\">"+
        "<title>Simple HTML+CSS progress bar</title>"+
        "<style>"+
        "</style>"+
    "</head>"+
    "<body>"+

"<h2>Just the HTML/CSS fallback (a <code>div</code> containing another <code>div</code>)</h2>"+

"<div id=\"progress\"  style=\"width: 500px; "+
                "height: 30px;"+
                "border: 1px solid #888; "+
                "background: rgb(168,168,168);"+
                "background: -moz-linear-gradient(top, rgba(168,168,168,1) 0%, rgba(204,204,204,1) 23%);"+
                "background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(168,168,168,1)), color-stop(23%,rgba(204,204,204,1)));"+
                "background: -webkit-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "background: -o-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "background: -ms-linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%);"+
                "filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a8a8a8', endColorstr='#cccccc',GradientType=0 );"+
                "background: linear-gradient(top, rgba(168,168,168,1) 0%,rgba(204,204,204,1) 23%); "+
                "position: relative;  \"><div id=\"bar\" style=\"height: 29px; "+
                "background: rgb(255,197,120); "+
                "background: -moz-linear-gradient(top, rgba(255,197,120,1) 0%, rgba(244,128,38,1) 100%); "+
                "background: -webkit-gradient(linear, left top, left bottom, color-stop(0%,rgba(255,197,120,1)), color-stop(100%,rgba(244,128,38,1))); "+
                "background: -webkit-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: -o-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: -ms-linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "background: linear-gradient(top, rgba(255,197,120,1) 0%,rgba(244,128,38,1) 100%); "+
                "border-top: 1px solid #fceabb; width:1%\"><p style=\"font-family: Arial, sans-serif;font-weight: bold;position: absolute; text-align: center; width: 100%; margin: 0; line-height: 30px;\"   >1%</p></div></div>"+

       " 100%(completo)"+
        
    "</body>"+
"</html>" , "US-ASCII", "html"
            
    );
    content.addBodyPart(mainPart);
    m.setContent(content);
    m.setSubject("HtmlTeste4");
    
    return m;
    
  }
  
  

  //
  //
  // Message sending methods
  //
  //
  
  /**
   * Send the message with Transport.send(Message)
   * 
   * @param message
   * @param recipient
   * @throws MessagingException
   */
  public static void addressAndSendMessage(Message message, String recipient)
      throws AddressException, MessagingException {
    message.setRecipient(RecipientType.TO, new InternetAddress(recipient));
    Transport.send(message);
  }

  /**
   * Send the message with Transport.send(Message, Address[])
   * 
   * @param message
   * @param recipient
   * @throws MessagingException
   */
  public static void sendMessageToAddress(Message message, String recipient)
      throws MessagingException {
    InternetAddress[] recipients = { new InternetAddress(recipient) };
    Transport.send(message, recipients);
  }

}
