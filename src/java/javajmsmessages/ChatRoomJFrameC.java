package javajmsmessages;

import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author Администратор
 */
public class ChatRoomJFrameC extends javax.swing.JFrame implements MessageListener {

    private static final Logger LOG = Logger.getLogger(ChatRoomJFrameC.class.getName());

    private TopicConnectionFactory connectionFactory;
    private TopicConnection connection;
    private TopicSession pubSession;
    private TopicSession subSession;
    private TopicPublisher publisher;
    private TopicSubscriber subscriber;

    //method handler 
    private void set(TopicSession pubSession, TopicSession subSession,
            TopicConnection connection, TopicPublisher publisher,
            TopicSubscriber subscriber) throws JMSException {
        this.pubSession = pubSession;
        this.subSession = subSession;
        this.connection = connection;
        this.publisher = publisher;
        this.subscriber = subscriber;
        subscriber.setMessageListener(this);
    }

    private void close() {
        try {
            connection.close();
        } catch (JMSException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    private void writeMessage(String text) {
        try {
            TextMessage message = pubSession.createTextMessage(text);
            publisher.publish(message);
        } catch (JMSException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Creates new form ChatRoomJFrame
     */
    public ChatRoomJFrameC() {
        initComponents();
        this.setTitle("C");
        try {
            InitialContext context = new InitialContext();
            connectionFactory = (TopicConnectionFactory) context.lookup("AugustSpecial");
            connection = connectionFactory.createTopicConnection();
            pubSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            subSession = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = (Topic) context.lookup("August");
            publisher = pubSession.createPublisher(topic);
            subscriber = subSession.createSubscriber(topic);
//            subscriber.setMessageListener(this);
            set(pubSession, subSession, connection, publisher, subscriber);

            connection.start();

            tfInput.addActionListener((ActionEvent e) -> {
                sendAction();
            });

            btnSend.addActionListener((ActionEvent e) -> {
                sendAction();
            });

        } catch (JMSException | NamingException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        taContent = new javax.swing.JTextArea();
        tfInput = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        taContent.setColumns(20);
        taContent.setRows(5);
        jScrollPane1.setViewportView(taContent);

        btnSend.setText("Send");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tfInput, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSend)
                        .addGap(0, 4, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tfInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSend))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            LOG.log(java.util.logging.Level.SEVERE, null, ex.getMessage());
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ChatRoomJFrameC().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSend;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea taContent;
    private javax.swing.JTextField tfInput;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            taContent.append(msg.getText() + "\n");
        } catch (JMSException e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }

    private void sendAction() {
        String text = tfInput.getText().trim();
        if (!text.equalsIgnoreCase("exit")) {
            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String timeStr = dateFormat.format(new Date());
            writeMessage(getTitle() + "  [" + timeStr + "]:  " + text);
            tfInput.setText("");
        } else {
            close();
            System.exit(0);
        }
    }
}
