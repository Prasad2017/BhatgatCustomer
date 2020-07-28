package com.graminvikreta.interfaces;


public interface OtpReceivedInterface {
  void onOtpReceived(String otp);
  void onOtpTimeout();
}
