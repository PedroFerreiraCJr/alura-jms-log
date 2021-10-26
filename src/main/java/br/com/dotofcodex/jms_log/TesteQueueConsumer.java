package br.com.dotofcodex.jms_log;

import java.util.Scanner;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;

public class TesteQueueConsumer {
	public static void main(String[] args) throws Exception {
		InitialContext ctx = new InitialContext();
		QueueConnectionFactory cf = (QueueConnectionFactory) ctx.lookup("ConnectionFactory");
		QueueConnection conexao = cf.createQueueConnection();
		conexao.start();

		QueueSession session = conexao.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		Queue fila = (Queue) ctx.lookup("LOG");
		// Quando enviado as quatro mensagens do produtor de fila de log, mas mensagens de maior prioridade devem aparecer primeiro
		MessageConsumer consumer = session.createConsumer(fila);
		consumer.setMessageListener((Message message) -> {
			System.out.println(message.getClass());
			TextMessage text = (TextMessage) message;
			try {
				System.out.println(text.getText());
			} catch (JMSException e) {
				e.printStackTrace();
			}
		});

		Scanner scan = new Scanner(System.in);
		scan.nextLine();

		scan.close();
		session.close();
		conexao.close();
		ctx.close();
	}
}
