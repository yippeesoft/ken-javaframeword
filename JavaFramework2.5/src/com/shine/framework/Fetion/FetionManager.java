package com.shine.framework.Fetion;

import net.solosky.maplefetion.FetionClient;
import net.solosky.maplefetion.LoginState;
import net.solosky.maplefetion.NotifyEventListener;
import net.solosky.maplefetion.bean.Message;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.NotifyEvent;
import net.solosky.maplefetion.event.NotifyEventType;
import net.solosky.maplefetion.event.action.ActionEventFuture;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.failure.RequestFailureEvent;
import net.solosky.maplefetion.event.action.success.SendChatMessageSuccessEvent;
import net.solosky.maplefetion.event.notify.ImageVerifyEvent;

import com.shine.framework.Fetion.utils.FetionClientMap;

/**
 * fetion 操作类
 * 
 * @author viruscodecn@gmail.com
 * @lib maplefetion-core-2.5.1.jar
 * 
 */
public class FetionManager {
	private static FetionManager manager = null;

	private static FetionClientMap map = null;

	public static FetionManager getManger() {
		if (manager == null) {
			manager = new FetionManager();
			map = new FetionClientMap();
		}
		return manager;
	}

	/**
	 * 登陆飞信
	 * 
	 * @param mobileNum
	 * @param password
	 */
	public void login(String mobileNum, String password) throws Exception {
		final FetionClient client = new FetionClient(mobileNum, password);
		// 这里设置一个登录状态监听器，可以显示当前登录步骤，避免用户感到焦虑
		client.setNotifyEventListener(new NotifyEventListener() {
			public void fireEvent(NotifyEvent event) {
				System.err.println(event.toString());
				// 如果出现验证码，取消登录
				if (event.getEventType() == NotifyEventType.IMAGE_VERIFY) {
					System.out.println("当前登录过程或者操作需要验证，操作失败。");
					client.cancelVerify((ImageVerifyEvent) event);
				}
			}
		});
		// 禁用掉群，登录可以变得快一点
		client.enableGroup(false);
		LoginState state = client.syncLogin();
		if (state == LoginState.LOGIN_SUCCESS) {
			System.err.println(mobileNum + "登陆成功");
			map.put(mobileNum, client);
		} else {
			System.err.println(mobileNum + "登陆失败");
		}
	}

	/**
	 * 发送飞信
	 * 
	 * @param mobileNum
	 *            发送号码
	 * @param tagetNum
	 *            目标号码
	 * @param messageContent
	 *            短信内容
	 */
	public void send(String mobileNum, String tagetNum, String messageContent)
			throws Exception {
		ActionEventFuture future = new ActionEventFuture(); // 建立一个Future来等待操作事件
		map.get(mobileNum).sendChatMessage(Long.parseLong(tagetNum),
				new Message(messageContent), future);
		ActionEvent event = future.waitActionEventWithoutException(); // 等待操作完成事件
		switch (event.getEventType()) {

		case SUCCESS:
			SendChatMessageSuccessEvent evt = (SendChatMessageSuccessEvent) event;
			if (evt.isSendToMobile()) {
				System.out.println("发送成功，消息已通过短信发送到对方手机！");
			} else if (evt.isSendToClient()) {
				System.out.println("发送成功，消息已通过服务直接发送到对方客户端！");
			}
			break;

		case FAILURE:
			FailureEvent evt2 = (FailureEvent) event;
			switch (evt2.getFailureType()) {
			case BUDDY_NOT_FOUND:
				System.out.println("发送失败, 该用户可能不是你好友，请尝试添加该用户为好友后再发送消息。");
				break;
			case USER_NOT_FOUND:
				System.out.println("发送失败, 该用户不是移动用户。");
				break;
			case SIPC_FAIL:
				System.out.println("发送失败, 服务器返回了错误的信息。");
				break;
			case UNKNOWN_FAIL:
				System.out.println("发送失败, 不知道错在哪里。");

			case REQEUST_FAIL:
				RequestFailureEvent evt3 = (RequestFailureEvent) event;
				System.out.println("提示:" + evt3.getReason() + ", 更多信息请访问:"
						+ evt3.getReason());

			default:
				System.out.println("发送消息失败！" + event.toString());
			}
			break;

		/*
		 * 以下三个错误状态是在异步发送消息的情况才会发生，
		 * 为了方便处理，使用waitActionEventWithException()同步的情况下，这三个错误是通过异常来处理的
		 * 也就是在waitActionEvent的时候就会判断是否出现了这三个错误，如果出现了就会抛出相应的异常
		 * 而waitActionEventWithoutException()不会抛出异常，会把这些错误作为操作事件返回
		 */
		case SYSTEM_ERROR:
			System.out.println("发送失败, 客户端内部错误。");
			break;
		case TIMEOUT:
			System.out.println("发送失败, 超时");
			break;
		case TRANSFER_ERROR:
			System.out.println("发送失败, 超时");
		}
	}

	/**
	 * 退出飞信
	 * 
	 * @param mobileNum
	 * @throws Exception
	 */
	public void loginOut(String mobileNum) throws Exception {
		System.out.print("正在退出客户端...");
		map.get(mobileNum).logout();
		System.out.println("已经退出。");
	}
}
