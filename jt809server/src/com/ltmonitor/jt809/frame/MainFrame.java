package com.ltmonitor.jt809.frame;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import net.miginfocom.swing.MigLayout;

import com.ltmonitor.entity.T809Constants;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.model.JT809Message;
import com.ltmonitor.jt809.model.ParameterModel;
import com.ltmonitor.jt809.tool.Tools;
import com.ltmonitor.jt809.tool.log4j.GUIPrintStream;

public class MainFrame extends JFrame {
	// public static JTextArea dataArea;
	public static TextAreaMenu dataArea;
	private JMenuItem databaseMenu;
	private JMenuItem exitSystemMenu;
	private JMenu jMenu1;
	private JMenu jMenu2;
	private JMenu jMenu3;
	private JMenu jMenu4;
	private JMenu jMenu5;
	private JMenuBar jMenuBar1;
	private JMenuItem jMenuItem3;
	private JScrollPane jScrollPane1;
	private JMenuItem parameterMenu;
	private JMenuItem queryAckMenu;
	private JMenuItem softwareMenu;
	private JPanel panel;
	private JButton btnConnect;
	private JTextField textField_Port;
	private JLabel lblNewLabel;
	private JCheckBox checkBox;
	private JButton btnStopServer;
	private JLabel lblip;
	private JTextField textField_ServerIP;
	private JScrollPane scrollPane;
	private JTable table;
	private JButton btnCloseMainLink;
	private JButton btnConnectMainLink;
	private JButton buttonClearLog;
	private JCheckBox checkBox_HideHeartLog;
	private JTextField textFilterByPlateNo;
	// 是否不显示心跳日志
	//private boolean hideHeartBeatLog = false;

	private static Logger logger = Logger.getLogger(MainFrame.class);

	public MainFrame() {
		setBackground(Color.LIGHT_GRAY);
		initComponents();
		initParameter();
	}

	private void initParameter() {
		setTitle("JT/T 809平台数据交换服务器");
		// setIconImage(Toolkit.getDefaultToolkit().getImage("E:\\\u4EA4\u901A\u90E8\u68C0\u6D4B\\809Java\u7248\u672C\\809GNSS\\user_go.png"));
	}

	private void initComponents() {
		this.jMenu3 = new JMenu();
		this.jScrollPane1 = new JScrollPane();
		// dataArea = new JTextArea();
		dataArea = new TextAreaMenu();
		this.jMenuBar1 = new JMenuBar();
		this.jMenu1 = new JMenu();
		this.exitSystemMenu = new JMenuItem();
		this.jMenu2 = new JMenu();
		this.jMenu4 = new JMenu();
		this.queryAckMenu = new JMenuItem();
		this.jMenuItem3 = new JMenuItem();
		this.jMenu5 = new JMenu();
		this.softwareMenu = new JMenuItem();

		this.jMenu3.setText("jMenu3");

		setDefaultCloseOperation(3);

		dataArea.setColumns(20);
		dataArea.setRows(5);
		this.jScrollPane1.setViewportView(dataArea);

		this.jMenu1.setText("\u8BBE\u7F6E");

		this.exitSystemMenu.setText("退出");
		this.exitSystemMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.exitSystemMenuActionPerformed(evt);
			}
		});
		this.parameterMenu = new JMenuItem();
		jMenu1.add(parameterMenu);

		this.parameterMenu.setText("系统参数设置");
		this.parameterMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.parameterMenuActionPerformed(evt);
			}
		});
		this.databaseMenu = new JMenuItem();
		jMenu1.add(databaseMenu);

		this.databaseMenu.setText("数据库参数设置");
		this.databaseMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.databaseMenuActionPerformed(evt);
			}
		});
		this.jMenu1.add(this.exitSystemMenu);

		this.jMenuBar1.add(this.jMenu1);

		this.jMenu2.setText("参数设置");

		this.jMenuBar1.add(this.jMenu2);

		this.jMenu4.setText("\u547D\u4EE4\u4E0B\u53D1");

		this.queryAckMenu.setText("查岗应答");
		this.queryAckMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.queryAckMenuActionPerformed(evt);
			}
		});
		this.jMenu4.add(this.queryAckMenu);

		this.jMenuItem3.setText("数据控制");
		this.jMenuItem3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.jMenuItem3ActionPerformed(evt);
			}
		});
		this.jMenu4.add(this.jMenuItem3);

		this.jMenuBar1.add(this.jMenu4);

		this.jMenu5.setText("\u5E2E\u52A9");

		this.softwareMenu.setText("软件版本");
		this.softwareMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				MainFrame.this.softwareMenuActionPerformed(evt);
			}
		});
		this.jMenu5.add(this.softwareMenu);

		this.jMenuBar1.add(this.jMenu5);

		setJMenuBar(this.jMenuBar1);

		panel = new JPanel();
		jScrollPane1.setColumnHeaderView(panel);
		panel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		lblip = new JLabel("\u8FD0\u7BA1\u670D\u52A1\u5668IP\uFF1A");
		panel.add(lblip);

		textField_ServerIP = new JTextField();
		textField_ServerIP.setColumns(10);
		panel.add(textField_ServerIP);

		lblNewLabel = new JLabel("\u7AEF\u53E3\uFF1A");
		panel.add(lblNewLabel);

		textField_Port = new JTextField();
		panel.add(textField_Port);
		textField_Port.setColumns(5);

		btnConnect = new JButton("启动");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ConnectMainLink();
			}
		});
		panel.add(btnConnect);

		btnStopServer = new JButton("\u505C\u6B62");
		btnStopServer.setEnabled(false);
		btnStopServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StopServer();
			}
		});
		getContentPane().setLayout(
				new MigLayout("", "[500px,grow]", "[329px][grow]"));
		panel.add(btnStopServer);

		checkBox = new JCheckBox("加密");
		checkBox.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				T809Manager.encrypt = true; // 设置加密标志
			}
		});
		panel.add(checkBox);

		btnCloseMainLink = new JButton("关闭主链路");
		btnCloseMainLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				closeMainLinkButtonActionPerformed(arg0);
			}
		});

		btnConnectMainLink = new JButton("连接主链路");
		btnConnectMainLink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				connectMainLink();
			}
		});

		buttonClearLog = new JButton("清空日志");
		buttonClearLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.close();
				System.setOut(new PrintStream(new GUIPrintStream(System.out,
						MainFrame.dataArea)));
				MainFrame.this.dataArea.setText("");
				DefaultTableModel tb = (DefaultTableModel) table.getModel();
				tb.setRowCount(0);
			}
		});

		checkBox_HideHeartLog = new JCheckBox(
				"显示日志");
		checkBox_HideHeartLog.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				boolean hideHeartBeatLog = checkBox_HideHeartLog.isSelected();
				GlobalConfig.displayMsg = hideHeartBeatLog;
			}
		});

		textFilterByPlateNo = new JTextField();
		textFilterByPlateNo.getDocument().addDocumentListener(
				new DocumentListener() {
					public void changedUpdate(DocumentEvent e) {
						warn();
					}

					public void removeUpdate(DocumentEvent e) {
						warn();
					}

					public void insertUpdate(DocumentEvent e) {
						warn();
					}

					public void warn() {
						// JOptionPane.showMessageDialog(null,
						// "系统将只显示卡号为:"+textFilterBySimNo.getText()+"的数据包");
						GlobalConfig.filterPlateNo = textFilterByPlateNo.getText();
					}
				});

		textFilterByPlateNo.setText("");
		textFilterByPlateNo.setColumns(15);
		
		panel.add(checkBox_HideHeartLog);
		panel.add(textFilterByPlateNo);
		panel.add(buttonClearLog);
		panel.add(btnConnectMainLink);
		panel.add(btnCloseMainLink);
		getContentPane().add(jScrollPane1, "cell 0 0,grow");

		scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, "cell 0 1,grow");

		table = new JTable();
		table.setFont(new Font("宋体", Font.PLAIN, 14));
		table.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, null }, }, new String[] {
				"\u6D88\u606F\u7C7B\u578B", "\u5B50\u6807\u8BC6",
				"\u6D88\u606F\u5185\u5BB9", "\u53D1\u9001\u65F6\u95F4",
				"\u8F66\u724C\u53F7", "\u989C\u8272",
				"\u539F\u59CB\u6570\u636E\u62A5\u6587" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(65);
		table.getColumnModel().getColumn(0).setMaxWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(260);
		table.getColumnModel().getColumn(1).setMaxWidth(1000);
		table.getColumnModel().getColumn(2).setPreferredWidth(275);
		table.getColumnModel().getColumn(2).setMaxWidth(1050);
		table.getColumnModel().getColumn(3).setPreferredWidth(155);
		table.getColumnModel().getColumn(3).setMaxWidth(400);
		table.getColumnModel().getColumn(4).setPreferredWidth(95);
		table.getColumnModel().getColumn(4).setMaxWidth(450);
		table.getColumnModel().getColumn(5).setPreferredWidth(63);
		table.getColumnModel().getColumn(5).setMaxWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(187);
		scrollPane.setViewportView(table);

		pack();

		// 初始化系统
		GlobalConfig gnss = new GlobalConfig();

		gnss.initSystem();
		
		ParameterModel pm = GlobalConfig.parModel;
		this.textField_ServerIP.setText(pm.getPlatformIP());
		this.textField_Port.setText("" + pm.getPlatformPort());

		final Component c = this;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(c, "确定要退出吗？", " 提示",
						JOptionPane.OK_CANCEL_OPTION);
				if (JOptionPane.OK_OPTION == option) {
					try {
						T809Manager.StopServer();
						if(timer != null)
							timer.stop();
					} catch (Exception ex) {

						logger.error(ex.getMessage(),ex);
						
					}
				} else {

					MainFrame.this
							.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				}
			}
		});

	}

	Timer timer = null;
	private void connectMainLink() {
		boolean res = T809Manager.UpConnectReq();
		if (res) {
			this.btnConnect.setEnabled(false);
			;
			this.btnStopServer.setEnabled(true);
			this.btnCloseMainLink.setEnabled(true);
			if (timer == null) {
				ActionListener taskPerformer = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {

						JT809Message tm = GlobalConfig.pollMsg();
						while (tm != null)
						{
							MainFrame.this.showMsg(tm);
							tm = GlobalConfig.pollMsg();
						}
					}
				};

				timer = new Timer(100, taskPerformer);
				timer.setRepeats(true);
				timer.start();
			}
		}
	}

	private void ConnectMainLink() {

		ParameterModel pm = GlobalConfig.parModel;
		pm.setPlatformIP(textField_ServerIP.getText());
		pm.setPlatformPort(Integer.parseInt(this.textField_Port.getText()));
		try {
			//启动JT809主从链路
			Boolean res = T809Manager.StartServer();
			if (res == false) {
				JOptionPane.showMessageDialog(null, "无法连接上级平台");
				return;
			}

			this.btnConnect.setEnabled(false);
			this.btnStopServer.setEnabled(true);
		} catch (Exception ex) {
			logger.error(ex.getMessage(),ex);
		}

	}

	private void StopServer() {
		T809Manager.StopServer();

		this.btnStopServer.setEnabled(false);
		this.btnCloseMainLink.setEnabled(false);
		this.btnConnect.setEnabled(true);

	}

	/**
	 * 关闭主链路
	 * 
	 * @param evt
	 */
	private void closeMainLinkButtonActionPerformed(ActionEvent evt) {
		boolean res = T809Manager.UpDisconnectReq();
		if (res) {
			this.btnStopServer.setEnabled(false);
			this.btnCloseMainLink.setEnabled(false);
			this.btnConnect.setEnabled(true);
		}
	}

	private void exitSystemMenuActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	private void softwareMenuActionPerformed(ActionEvent evt) {
		SoftVerFrame soft = new SoftVerFrame(null, true);
		soft.setLocationRelativeTo(null);
		soft.setVisible(true);
	}

	private void databaseMenuActionPerformed(ActionEvent evt) {

	}

	private void parameterMenuActionPerformed(ActionEvent evt) {
		ParameterFrame pf = new ParameterFrame(null, true);
		pf.setLocationRelativeTo(null);
		pf.setVisible(true);
	}

	private void queryAckMenuActionPerformed(ActionEvent evt) {
		PlatformMsgPostQueryAckFrame pm = new PlatformMsgPostQueryAckFrame(
				null, true);
		pm.setLocationRelativeTo(null);
		pm.setVisible(true);
	}

	private void jMenuItem3ActionPerformed(ActionEvent evt) {
		ControlFrame cf = new ControlFrame();
		cf.setLocationRelativeTo(null);
		cf.setVisible(true);
	}

	private void showMsg(JT809Message tm) {
		/**
		if (this.hideHeartBeatLog) {
			if (tm.getMsgType() == 0x1005 || tm.getMsgType() == 0x1006
					|| tm.getMsgType() == 0x1200
							|| tm.getMsgType() == 0x1400 
					|| tm.getMsgType() == 0x9005 || tm.getMsgType() == 0x9006)
				return;
		}*/
		Date now = new Date();
		Integer subType = tm.getSubType() == 0 ? tm.getMsgType() : tm
				.getSubType();
		String subDescr = T809Constants.getMsgDescr(subType);

		subDescr = "[" + "0x" + Tools.ToHexString(subType, 2) + "]" + subDescr;

		DefaultTableModel tb = (DefaultTableModel) this.table.getModel();
		tb.insertRow(
				0,
				new Object[] { "0x" + Tools.ToHexString(tm.getMsgType(), 2),
						subDescr, tm.getDescr(), now.toLocaleString(),
						tm.getPlateNo(), tm.getPlateColor(),
						tm.getPacketDescr() });
		

		if(table.getRowCount() > 1000)
			tb.setRowCount(10);
		table.setRowHeight(0, 35);
	}

	public static void main(String[] args) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager
					.getInstalledLookAndFeels())
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
}
