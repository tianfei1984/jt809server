package com.ltmonitor.jt809.frame;

import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

import com.ltmonitor.entity.GnssData;
import com.ltmonitor.entity.WarnData;
import com.ltmonitor.jt809.app.GlobalConfig;
import com.ltmonitor.jt809.app.T809Manager;
import com.ltmonitor.jt809.app.VehiclePlateColor;
import com.ltmonitor.jt809.entity.DriverModel;
import com.ltmonitor.jt809.entity.VehicleRegisterInfo;
import com.ltmonitor.jt809.model.VehicleModel;


public class ControlFrame extends JFrame {
	private JButton registerVehicleButton;
	private JButton btnApplyForMonitorStartUp;
	private JTextField txtPlateNo;
	private JComboBox cmbPlateColor;

	public ControlFrame() {
		initComponents();
		initFrame();
		initParameter();
	}

	private void initFrame() {
		setTitle("\u4EA4\u6362\u6570\u636E\u4FE1\u606F");
		setIconImage(new ImageIcon(getClass().getResource(
				"/com/ltmonitor/jt809/frame/20111108112654.png")).getImage());
	}

	private void initParameter() {
	}

	private void initComponents() {
		this.btnApplyForMonitorStartUp = new JButton();
		this.registerVehicleButton = new JButton();

		setDefaultCloseOperation(2);

		this.btnApplyForMonitorStartUp
				.setText("\u7533\u8BF7\u4EA4\u6362\u8F66\u8F86\u4FE1\u606F");
		this.btnApplyForMonitorStartUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ControlFrame.this.applyForMonitorStartUp(evt);
			}
		});
		this.registerVehicleButton.setText("\u6CE8\u518C\u8F66\u8F86");
		this.registerVehicleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				ControlFrame.this.registerVehicleButtonActionPerformed(evt);
			}
		});
		
		JButton btnEndApplyForMonitor = new JButton();
		btnEndApplyForMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				applyForMonitorEnd(arg0);
			}
		});
		btnEndApplyForMonitor.setText("取消交换车辆信息");
		
		JButton buttonUP_EXG_MSG_APPLY_HISGNSSDATA_REQ = new JButton();
		buttonUP_EXG_MSG_APPLY_HISGNSSDATA_REQ.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				applyHistoryGnssDataButtonActionPerformed(arg0);
			}
		});
		buttonUP_EXG_MSG_APPLY_HISGNSSDATA_REQ.setText("\u8BF7\u6C42\u8865\u53D1\u8F66\u8F86\u5B9A\u4F4D(1209)");
		
		JButton buttonUpExgMsgRealLocation = new JButton();
		buttonUpExgMsgRealLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				upExgMsgRealLocationButtonActionPerformed(arg0);
			}
		});
		buttonUpExgMsgRealLocation.setText("\u5B9E\u65F6\u4E0A\u4F20\u8F66\u8F86\u5B9A\u4F4D\u4FE1\u606F");
		
		JButton buttonUpTakeEWayBill = new JButton();
		buttonUpTakeEWayBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				upExgMsgReportTakeEWayBillPerformed(arg0);
			}
		});
		buttonUpTakeEWayBill.setText("\u4E3B\u52A8\u4E0A\u62A5\u7535\u5B50\u8FD0\u5355");
		
		txtPlateNo = new JTextField();
		txtPlateNo.setText("\u6D4BA90001");
		txtPlateNo.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("\u8F66\u724C\u53F7");
		
		JLabel label = new JLabel("\u8F66\u724C\u989C\u8272");
		
		cmbPlateColor = new JComboBox();
		cmbPlateColor.setModel(new DefaultComboBoxModel(VehiclePlateColor.values()));
		
		JButton buttonSendWarnInfo = new JButton();
		buttonSendWarnInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendWarnInfo(arg0);
			}
		});
		buttonSendWarnInfo.setText("\u53D1\u9001\u62A5\u8B66\u4FE1\u606F1402");
		
		JButton buttonAddVehicleInfo = new JButton();
		buttonAddVehicleInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addVehicle(e);
			}
		});
		buttonAddVehicleInfo.setText("\u8865\u62A5\u8F66\u8F86\u9759\u6001\u6570\u636E1402");
		
		JButton buttonSendHistoryLocation = new JButton();
		buttonSendHistoryLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendHistoryLocation(e);
			}
		});
		buttonSendHistoryLocation.setText("\u5B9A\u4F4D\u4FE1\u606F\u81EA\u52A8\u8865\u62A5");
		
		JButton buttonUpDriver = new JButton("\u4E0A\u62A5\u9A7E\u9A76\u5458\u4FE1\u606F");
		buttonUpDriver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendDriverInfo(arg0);
			}
		});
		
		JButton buttonSendWarnToDoInfo = new JButton();
		buttonSendWarnToDoInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				sendWarnToDoInfo(arg0);
			}
		});
		buttonSendWarnToDoInfo.setText("\u4E3B\u52A8\u4E0A\u62A5\u62A5\u8B66\u5904\u7406\u7ED3\u679C");
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addGap(37)
							.addComponent(lblNewLabel)
							.addGap(18)
							.addComponent(txtPlateNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnApplyForMonitorStartUp)
						.addComponent(buttonSendWarnInfo, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonUP_EXG_MSG_APPLY_HISGNSSDATA_REQ, GroupLayout.PREFERRED_SIZE, 177, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
							.addComponent(buttonSendWarnToDoInfo)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(buttonUpTakeEWayBill, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(buttonUpDriver))
						.addGroup(layout.createSequentialGroup()
							.addComponent(label, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(cmbPlateColor, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
							.addComponent(btnEndApplyForMonitor, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(registerVehicleButton)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonAddVehicleInfo, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE))
						.addGroup(layout.createSequentialGroup()
							.addComponent(buttonUpExgMsgRealLocation, GroupLayout.PREFERRED_SIZE, 157, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonSendHistoryLocation, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)))
					.addGap(174))
		);
		layout.setVerticalGroup(
			layout.createParallelGroup(Alignment.LEADING)
				.addGroup(layout.createSequentialGroup()
					.addContainerGap()
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(label)
							.addComponent(cmbPlateColor, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(txtPlateNo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblNewLabel))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(layout.createParallelGroup(Alignment.LEADING)
						.addGroup(layout.createParallelGroup(Alignment.BASELINE)
							.addComponent(btnEndApplyForMonitor)
							.addComponent(registerVehicleButton)
							.addComponent(buttonAddVehicleInfo))
						.addComponent(btnApplyForMonitorStartUp))
					.addGap(14)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonUP_EXG_MSG_APPLY_HISGNSSDATA_REQ)
						.addComponent(buttonUpExgMsgRealLocation)
						.addComponent(buttonSendHistoryLocation))
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGroup(layout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonSendWarnInfo)
						.addComponent(buttonSendWarnToDoInfo)
						.addComponent(buttonUpTakeEWayBill)
						.addComponent(buttonUpDriver))
					.addContainerGap())
		);
		getContentPane().setLayout(layout);

		pack();
	}
	//定位信息自动补报
	private void sendHistoryLocation(ActionEvent evt){
		//模拟五条定位数据
		java.util.List<GnssData> gnssDatas = new java.util.ArrayList<GnssData>();
		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		GnssData gd = new GnssData();
		gd.setPlateNo(plateNo);
		gd.setPlateColor(color);
		gnssDatas.add(gd);
		gd = new GnssData();
		gd.setPlateNo(plateNo);
		gd.setPlateColor(color);
		gnssDatas.add(gd);
		gd = new GnssData();
		gd.setPlateNo(plateNo);
		gd.setPlateColor(color);
		gnssDatas.add(gd);
		gd = new GnssData();
		gd.setPlateNo(plateNo);
		gd.setPlateColor(color);
		gnssDatas.add(gd);
		gd = new GnssData();
		gd.setPlateNo(plateNo);
		gd.setPlateColor(color);
		gnssDatas.add(gd);

		boolean res = T809Manager.UpExgMsgHistoryLocations(plateNo, color,gnssDatas);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	//发送报警信息
	private void sendDriverInfo(ActionEvent evt)
	{
		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		DriverModel dm = new DriverModel();	
		dm.setPlateColor(color);
		dm.setPlateNo(plateNo);
		boolean res = T809Manager.UpExgMsgReportDriverInfo(dm);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	
	//发送报警信息
	private void sendWarnInfo(ActionEvent evt)
	{
		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		WarnData wd = new WarnData();
		String content = "前面出现交通事故";
		
		wd.setPlateColor(color);
		wd.setWarnTime(new Date());
		wd.setPlateNo(plateNo);
		wd.setContent(content);
		wd.setSrc(1);
		wd.setType(1);
		
		boolean res = T809Manager.UpWarnMsgAdptInfo(wd);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	
	//发送报警信息
	private void sendWarnToDoInfo(ActionEvent evt)
	{
		WarnData wd = new WarnData();

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		String content = "前面出现交通事故";
		
		wd.setPlateColor(color);
		wd.setWarnTime(new Date());
		wd.setPlateNo(plateNo);
		wd.setContent(content);
		wd.setSrc(1);
		wd.setType(1);
		wd.setInfoId(12);
		wd.setResult(1);
		
		boolean res = T809Manager.UpWarnMsgAdptToDoInfo(wd.getPlateNo(), wd.getPlateColor(), wd.getInfoId(),wd.getResult());
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	
	private void addVehicle(ActionEvent evt)
	{
		VehicleModel vm = new VehicleModel();

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		vm.setPlateNo(plateNo);
		vm.setPlateColor(color);
		T809Manager.UpBaseMsgVehicleAddedAck(vm);
	}

	private void applyForMonitorStartUp(ActionEvent evt) {

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		Date end = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, -1);
		Date start = calendar.getTime();
	
		boolean res = T809Manager.UpExgMsgApplyForMonitorStartup(plateNo, (byte)color, start, end);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	
	private void applyForMonitorEnd(ActionEvent evt) {
		String PlateNo = "苏A53251";
		byte color = 1;
		boolean res = T809Manager.UpExgMsgApplyForMonitorEnd(PlateNo, color);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	

	private void registerVehicleButtonActionPerformed(ActionEvent evt) {
		//dispose();

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		
		VehicleRegisterInfo vm = new VehicleRegisterInfo();
		vm.setTerminalId("1234567");
		vm.setPlateNo(plateNo);
		vm.setSimNo("13942578800");
		vm.setPlateColor(color);
		boolean res = T809Manager.UpExgMsgRegister(vm);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}
	

	//申请定位信息补发
	private void applyHistoryGnssDataButtonActionPerformed(ActionEvent evt) {

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		VehicleModel vm = new VehicleModel();
		vm.setPlateColor(color);
		vm.setPlateNo(plateNo);
		Date start = new Date();
		Date end = new Date();
		boolean res = T809Manager.UpExgMsgApplyHisGnssDataReq(vm.getPlateNo(),
				(byte)vm.getPlateColor(), start, end);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}

	//实时发送定位信息
	private void upExgMsgRealLocationButtonActionPerformed(ActionEvent evt) {

		String plateNo= txtPlateNo.getText();
		int color = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		GnssData gnssData = new GnssData();
		gnssData.setPlateColor(color);
		gnssData.setPlateNo(plateNo);
		boolean res = T809Manager.UpExgMsgRealLocation(gnssData);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
	}

	//电子运单
	private void upExgMsgReportTakeEWayBillPerformed(ActionEvent evt) {

		String plateNo= txtPlateNo.getText();
		int plateColor = Integer.parseInt(""+cmbPlateColor.getSelectedItem());
		String eContent = "电子运单,南岗油库，油品运输，20吨91#汽油，松山加油站";
		boolean res = T809Manager.UpExgMsgReportTakeEWayBill(plateNo, plateColor, eContent);
		JOptionPane.showMessageDialog(null, res ? "发送成功" : "发送失败");
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
			Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		} catch (UnsupportedLookAndFeelException ex) {
			Logger.getLogger(ControlFrame.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				new ControlFrame().setVisible(true);
			}
		});
	}
}
