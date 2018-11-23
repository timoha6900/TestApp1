package com.testapp.readysteadybang.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllowModel {

	@SerializedName("allow")
	@Expose
	private Boolean allow;

	public Boolean getAllow(){
		return allow;
	}

	public void setAllow(Boolean allow){
		this.allow = allow;
	}

}
