package tukano.servers.java;

import tukano.api.Discovery;
import tukano.api.java.Hibernate;
import tukano.api.java.Result;
import tukano.api.java.Result.ErrorCode;
import tukano.api.User;
import tukano.api.java.Users;

import java.util.*;
import java.util.logging.Logger;

public class JavaUsers implements Users {
	private final Map<String,User> users = new HashMap<>();

	private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

	@Override
	public Result<String> createUser(User user) {
		Log.info("createUser : " + user);
		// Check if user data is valid
		if(user.userId() == null || user.pwd() == null || user.displayName() == null || user.email() == null) {
			Log.info("User object invalid.");
			return Result.error( ErrorCode.BAD_REQUEST);
		}

		if(checkUserExists(user.getUserId())){
			Log.info("User already exists.");
			return Result.error( ErrorCode.CONFLICT);
		}

		Hibernate.getInstance().persist(user);

		return Result.ok( user.userId() );
	}
	private boolean checkUserExists(String userId){
		String sqlQuery = "SELECT * FROM User u WHERE u.userId="+ "'" +userId + "'";
		List<User> list = Hibernate.getInstance().sql(sqlQuery, User.class);
		return !list.isEmpty();
	}

	private boolean isPWDCorrect(String userId, String pwd){
		boolean answer = false;
		String sqlQuery = "SELECT * FROM User u WHERE u.userId=" + "'" + userId + "'";

		Log.info(sqlQuery);

		List<User> list = Hibernate.getInstance().sql(sqlQuery, User.class);

		User user = list.get(0);

		if(user.getPwd().equals(pwd)){
			answer = true;
		}
		return answer;
	}

	private User getUser(String userId){
		String sqlQuery = "SELECT * FROM User u WHERE u.userId=" + "'" + userId + "'";
		List<User> list = Hibernate.getInstance().sql(sqlQuery, User.class);
		return list.get(0);
	}

	@Override
	public Result<User> getUser(String userId, String pwd) {
		Log.info("getUser : user = " + userId + "; pwd = " + pwd);

		// Check if user is valid
		if(userId == null || pwd == null) {
			Log.info("Name or Password null.");
			return Result.error( ErrorCode.BAD_REQUEST);
		}

		// Check if user exists
		if(!checkUserExists(userId)){
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND);
		}

		//Check if the password is correct
		if( !isPWDCorrect(userId, pwd)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN);
		}
		User user = getUser(userId);

		return Result.ok(user);
	}

	@Override
	public Result<User> updateUser(String userId, String pwd, User user) {
		Log.info("updateUser : userid = " + userId + "; pwd = " + pwd + "; user = " + user);

		// Check if user is valid
		//userID can NOT be updated, if userID of received user is null it gets bad requested
		if(userId == null || pwd == null || user == null || user.getUserId()!=null) {
			Log.info("Name , Password or User null.");
			return Result.error( ErrorCode.BAD_REQUEST);
		}

		// Check if user exists
		if(!checkUserExists(userId)){
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND);
		}

		//Check if the password is correct
		if( !isPWDCorrect(userId, pwd)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN);
		}
		User previousUser = getUser(userId);

		if(user.getUserId() == null){
			user.setUserId(userId);
		}

		if(user.getPwd() == null){
			user.setPwd(pwd);
		}

		if(user.getDisplayName() == null){
			user.setDisplayName(previousUser.getDisplayName());
		}

		if (user.getEmail() == null) {
			user.setEmail(previousUser.getEmail());
		}

		Hibernate.getInstance().update(user);

		return Result.ok(user);
	}

	@Override
	public Result<User> deleteUser(String userId, String pwd) {
		//return Result.error( ErrorCode.NOT_IMPLEMENTED);

		Log.info("deleteUser : userid = " + userId + "; pwd = " + pwd);

		// Check if user is valid
		if(userId == null || pwd == null ) {
			Log.info("Name or Password null.");
			return Result.error( ErrorCode.BAD_REQUEST);
		}

		// Check if user exists
		if(!checkUserExists(userId)){
			Log.info("User does not exist.");
			return Result.error( ErrorCode.NOT_FOUND);
		}

		//Check if the password is correct
		if( !isPWDCorrect(userId, pwd)) {
			Log.info("Password is incorrect.");
			return Result.error( ErrorCode.FORBIDDEN);
		}

		User user = getUser(userId);

		Hibernate.getInstance().delete(user);

		return Result.ok(user);
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		String query =  "SELECT * FROM User u WHERE LOWER(u.userId) LIKE '%" + pattern.toLowerCase() + "%'";
		List<User> list = Hibernate.getInstance().sql(query, User.class);

		//set pwd to ""
		for(int i = 0 ; i < list.size(); i ++){
			list.get(i).setPwd("");
		}
		return Result.ok(list);
	}


}
