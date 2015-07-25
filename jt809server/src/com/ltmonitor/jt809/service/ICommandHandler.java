package com.ltmonitor.jt809.service;

import com.ltmonitor.entity.JT809Command;
import com.ltmonitor.entity.TerminalCommand;


public interface ICommandHandler {
	boolean OnRecvCommand(JT809Command tc);

}