package br.com.dotofcodex.jms_log;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;

/**
 * A versão do JMS utilizado nesta implementação é a versão 1.1;
 * 
 * @author Pedro Junior
 *
 */
public class TesteQueueProdutor {
	public static void main(String[] args) throws Exception {
		InitialContext context = new InitialContext();
		ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
		Connection conn = factory.createConnection();
		conn.start();

		Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination fila = (Destination) context.lookup("LOG");
		MessageProducer producer = session.createProducer(fila);

		
		//Message message = session.createTextMessage("DEBUG | Apache ActiveMQ 5.12.0 (localhost, ID:vostro-5470-46665-1634672281218-0:1) is starting");
		//Message message = session.createTextMessage("INFO | Apache ActiveMQ 5.12.0 (localhost, ID:vostro-5470-46665-1634672281218-0:1) is starting");
		//Message message = session.createTextMessage("WARN | Apache ActiveMQ 5.12.0 (localhost, ID:vostro-5470-46665-1634672281218-0:1) is starting");
		//Message message = session.createTextMessage("ERROR | Apache ActiveMQ 5.12.0 (localhost, ID:vostro-5470-46665-1634672281218-0:1) is starting");
		
		// A prioridade vai de 0 a 9.
		// DEBUG deve ter a prioridade 	- 0;
		// INFO deve ter a prioridade 	- 3;
		// WARN deve ter a prioridade 	- 7;
		// ERROR deve ter a prioridade 	- 9;
		
		/*
		Para que o ActiveMQ ordene as mensagens por ordem de prioridade informado no método send, é preciso adicionar essa configuração no arquivo: conf/activemq.zml
		<policyEntry queue=">" prioritizedMessages="true"/>
		*/
		
		Message message = session.createTextMessage("DEBUG | Apache ActiveMQ 5.12.0 (localhost, ID:vostro-5470-46665-1634672281218-0:1) is starting");
		// O DeliveryMode configura se a mensagem deve sobreviver ao desligamento do ActiveMQ
		// O terceiro parâmetro informa o valor da prioridade da mensagem
		// O Quarto parâmetro informa o tempo de vida da mensagem antes de ser removida da fila de mensagens, caso ainda não tenha sido entregue
		producer.send(message, DeliveryMode.NON_PERSISTENT, 3, 60_000L);

		session.close();
		conn.close();
		context.close();
	}
}
