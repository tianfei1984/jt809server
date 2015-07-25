package com.ltmonitor.jt809.frame;

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.entity.CheckRecord;
import com.ltmonitor.jt809.server.PlatformClient;
import com.ltmonitor.jt809.tool.Tools;


public class PlatformMsgPostQueryAckFrame extends JDialog {
	private static final long serialVersionUID = 1L;
	private JButton cancelButton;
	private JTextArea contentText;
	private JComboBox infoId;
	private JLabel jLabel1;
	private JLabel jLabel2;
	private JLabel jLabel3;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JTextArea messageText;
	private JButton sendButton;

	public PlatformMsgPostQueryAckFrame(Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		initFrame();
		initParameter();
	}

	private void initFrame() {
		setTitle("平台查岗信息");
		setIconImage(new ImageIcon(getClass().getResource(
				"/tm/base/frame/20111108112654.png")).getImage());
	}

	private void initParameter() {
		Collection c = GlobalConfig.chagang.values();
		Iterator it = c.iterator();

		int i = 0;
		while (it.hasNext()) {
			CheckRecord cm = (CheckRecord) it.next();
			this.infoId.addItem(cm.getInfoId());
			if (i == 0) {
				this.contentText.setText(cm.getMessage());
				this.messageText.setText("手工查岗应答:=问题|操作员|答案");
			}
			i++;
		}
	}

	private void initComponents() {
		this.jLabel1 = new JLabel();
		this.jLabel2 = new JLabel();
		this.sendButton = new JButton();
		this.cancelButton = new JButton();
		this.infoId = new JComboBox();
		this.jScrollPane2 = new JScrollPane();
		this.contentText = new JTextArea();
		this.jLabel3 = new JLabel();
		this.jScrollPane3 = new JScrollPane();
		this.messageText = new JTextArea();

		setDefaultCloseOperation(2);

		this.jLabel1.setText("消息ID：");

		this.jLabel2.setText("内容：");

		this.sendButton.setText("发送");
		this.sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				PlatformMsgPostQueryAckFrame.this
						.sendButtonActionPerformed(evt);
			}
		});
		this.cancelButton.setText("取消");
		this.cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				PlatformMsgPostQueryAckFrame.this
						.cancelButtonActionPerformed(evt);
			}
		});
		this.infoId.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent evt) {
				PlatformMsgPostQueryAckFrame.this.infoIdItemStateChanged(evt);
			}
		});
		this.infoId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				PlatformMsgPostQueryAckFrame.this.infoIdActionPerformed(evt);
			}
		});
		this.contentText.setColumns(20);
		this.contentText.setRows(5);
		this.jScrollPane2.setViewportView(this.contentText);

		this.jLabel3.setText("回复");

		this.messageText.setColumns(20);
		this.messageText.setRows(5);
		this.jScrollPane3.setViewportView(this.messageText);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout
				.setHorizontalGroup(layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(
								layout
										.createSequentialGroup()
										.addGroup(
												layout
														.createParallelGroup(
																GroupLayout.Alignment.LEADING,
																false)
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				this.jLabel1)
																		.addPreferredGap(
																				LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				this.infoId,
																				-2,
																				316,
																				-2))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addGap(
																				82,
																				82,
																				82)
																		.addComponent(
																				this.sendButton)
																		.addGap(
																				87,
																				87,
																				87)
																		.addComponent(
																				this.cancelButton))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				this.jScrollPane2))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				this.jLabel2))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				this.jLabel3))
														.addGroup(
																layout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addComponent(
																				this.jScrollPane3)))
										.addContainerGap(-1, 32767)));

		layout.setVerticalGroup(layout.createParallelGroup(
				GroupLayout.Alignment.LEADING).addGroup(
				layout.createSequentialGroup().addContainerGap().addGroup(
						layout.createParallelGroup(
								GroupLayout.Alignment.BASELINE).addComponent(
								this.jLabel1).addComponent(this.infoId, -2, -1,
								-2)).addPreferredGap(
						LayoutStyle.ComponentPlacement.RELATED).addComponent(
						this.jLabel2).addGap(12, 12, 12).addComponent(
						this.jScrollPane2, -2, -1, -2).addPreferredGap(
						LayoutStyle.ComponentPlacement.UNRELATED).addComponent(
						this.jLabel3).addPreferredGap(
						LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
						.addComponent(this.jScrollPane3, -2, -1, -2)
						.addPreferredGap(
								LayoutStyle.ComponentPlacement.UNRELATED)
						.addGroup(
								layout.createParallelGroup(
										GroupLayout.Alignment.BASELINE)
										.addComponent(this.sendButton)
										.addComponent(this.cancelButton))
						.addContainerGap()));

		pack();
	}

	private void cancelButtonActionPerformed(ActionEvent evt) {
		dispose();
	}

	private void sendButtonActionPerformed(ActionEvent evt) {
		String body = "";
		String mess = "";
		if ((this.messageText.getText() != null)
				&& (this.messageText.getText().length() > 0)) {
			CheckRecord cm = (CheckRecord) GlobalConfig.chagang
					.get(this.infoId.getSelectedItem().toString());
			CheckRecord pc = new CheckRecord(cm);
			
			pc.setMessage(this.messageText.getText());
			boolean res = T809Manager.UpPlatFormMsgPostQueryAck(pc);
			JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
		} else {
			this.messageText.setText("回复内容不能为空！");
		}
	}

	private void infoIdItemStateChanged(ItemEvent evt) {
		CheckRecord cm = (CheckRecord) GlobalConfig.chagang
				.get(this.infoId.getSelectedItem().toString());
		this.contentText.setText(cm.getMessage());
	}

	private void infoIdActionPerformed(ActionEvent evt) {
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels())
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(PlatformMsgPostQueryAckFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(PlatformMsgPostQueryAckFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(PlatformMsgPostQueryAckFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(PlatformMsgPostQueryAckFrame.class.getName()).log(
					Level.SEVERE, null, ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				PlatformMsgPostQueryAckFrame dialog = new PlatformMsgPostQueryAckFrame(
						new JFrame(), true);
				dialog.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
				dialog.setVisible(true);
			}
		});
	}
}
