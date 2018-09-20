package model;

import java.io.Serializable;

import com.sun.media.sound.SoftInstrument;

public class User implements Serializable {

	private String name;
	private String pass;
	private String id;
	public boolean login_status = false;

	public User() {
	}

	public User(String pass, String name) {

		this.pass = pass;
		this.name = name;

	}
	
	public User(String id,String pass, String name) {
		
		this.id = id;
		this.pass = pass;
		this.name = name;

	}

	public String getId() {

		return id;

	}

	public String getPass() {

		return pass;

	}

	public String getName() {

		return name;

	}

	public String setName(String name) {

		this.name = name;
		return name;

	}
	
	public String setId(String id) {

		this.id = (id);
		return id;

	}

	// ログインしているかを確認
	public boolean loginUser(int id, User user) {

		// userインスタンスのidとパラメータのidが同じならログイン状態とみなす

		if (id == Integer.parseInt(user.getId())) {

			login_status = true;
			return login_status;

		}

		login_status = false;
		return login_status;
	}

}