package model;

import java.io.Serializable;

import com.sun.media.sound.SoftInstrument;

public class User implements Serializable {

	private String name;
	private String pass;
	private String id;

	public User() {
	}

	public User(String id, String pass, String name) {

		this.id = id;
		this.pass = pass;
		this.name = name;

	}

	// 管理で使う
	public User(String id, String pass) {

		this.id = id;
		this.pass = pass;

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

}