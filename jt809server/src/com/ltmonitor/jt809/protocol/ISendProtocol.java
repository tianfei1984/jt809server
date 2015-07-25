package com.ltmonitor.jt809.protocol;

import com.ltmonitor.jt809.model.JT809Message;

public abstract interface ISendProtocol
{
  public abstract JT809Message wrapper();
}

