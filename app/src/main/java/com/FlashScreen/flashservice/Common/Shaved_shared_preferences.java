package com.FlashScreen.flashservice.Common;


import android.content.Context;
import android.content.SharedPreferences;

public class Shaved_shared_preferences {
	Context activity;
	public Shaved_shared_preferences(Context activity)
	{
		this.activity=activity;

	}

	public void set_user_weight(String weight){
		SharedPreferences prefs=activity.getSharedPreferences("weight", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("weight", weight);
		editor.commit();
	}
	public String get_user_weight(){
		SharedPreferences prefs =activity.getSharedPreferences("weight",activity.MODE_PRIVATE);
		String weight=prefs.getString("weight", null);
		return weight;
	}

	public void set_reload(String user_display){
		SharedPreferences prefs=activity.getSharedPreferences("user_display", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_display", user_display);
		editor.commit();
	}
	public String get_reload(){
		SharedPreferences prefs =activity.getSharedPreferences("user_display",activity.MODE_PRIVATE);
		String user_display=prefs.getString("user_display", null);
		return user_display;
	}


	public void set_user_age(String age){
		SharedPreferences prefs=activity.getSharedPreferences("age", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("age", age);
		editor.commit();
	}
	public String get_user_age(){
		SharedPreferences prefs =activity.getSharedPreferences("age",activity.MODE_PRIVATE);
		String age=prefs.getString("age", null);
		return age;
	}
	public void set_packId(String user_add){
		SharedPreferences prefs=activity.getSharedPreferences("packId", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("packId", user_add);
		editor.commit();
	}

	public String get_packId(){
		SharedPreferences prefs =activity.getSharedPreferences("packId",activity.MODE_PRIVATE);
		String user_add=prefs.getString("packId", null);
		return user_add;
	}

	public void set_packName(String user_add1){
		SharedPreferences prefs=activity.getSharedPreferences("packName", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("packName", user_add1);
		editor.commit();
	}

	public String get_packName(){
		SharedPreferences prefs =activity.getSharedPreferences("packName",activity.MODE_PRIVATE);
		String user_add1=prefs.getString("packName", null);
		return user_add1;
	}
	public void set_packIdT(String user_add){
		SharedPreferences prefs=activity.getSharedPreferences("packId", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("packId", user_add);
		editor.commit();
	}

	public String get_packIdT(){
		SharedPreferences prefs =activity.getSharedPreferences("packId",activity.MODE_PRIVATE);
		String user_add=prefs.getString("packId", "1");
		return user_add;
	}

	public void set_packNameT(String user_add1){
		SharedPreferences prefs=activity.getSharedPreferences("packName", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("packName", user_add1);
		editor.commit();
	}

	public String get_packNameT(){
		SharedPreferences prefs =activity.getSharedPreferences("packName",activity.MODE_PRIVATE);
		String user_add1=prefs.getString("packName", "Free");
		return user_add1;
	}

	public void set_user_cname(String user_cname){
		SharedPreferences prefs=activity.getSharedPreferences("user_cname", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_cname", user_cname);
		editor.commit();
	}
	public String get_user_cname(){
		SharedPreferences prefs =activity.getSharedPreferences("user_cname",activity.MODE_PRIVATE);
		String user_cname=prefs.getString("user_cname", null);
		return user_cname;
	}

	public void set_price(String height){
		SharedPreferences prefs=activity.getSharedPreferences("height", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("height", height);
		editor.commit();
	}
	public String get_price(){
		SharedPreferences prefs =activity.getSharedPreferences("height",activity.MODE_PRIVATE);
		String height=prefs.getString("height", null);
		return height;
	}

	public void set_user_add(String user_add){
		SharedPreferences prefs=activity.getSharedPreferences("user_add", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_add", user_add);
		editor.commit();
	}

	public String get_user_add(){
		SharedPreferences prefs =activity.getSharedPreferences("user_add",activity.MODE_PRIVATE);
		String user_add=prefs.getString("user_add", null);
		return user_add;
	}

	public void set_user_add1(String user_add1){
		SharedPreferences prefs=activity.getSharedPreferences("user_add1", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_add", user_add1);
		editor.commit();
	}

	public String get_user_add1(){
		SharedPreferences prefs =activity.getSharedPreferences("user_add1",activity.MODE_PRIVATE);
		String user_add1=prefs.getString("user_add1", null);
		return user_add1;
	}

	public void set_phone(String userphone){
		SharedPreferences prefs=activity.getSharedPreferences("userphone", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("userphone", userphone);
		editor.commit();
	}
	public String get_phone(){
		SharedPreferences prefs =activity.getSharedPreferences("userphone",activity.MODE_PRIVATE);
		String userphone=prefs.getString("userphone", null);
		return userphone;
	}

	public void set_user_state(String user_state){
		SharedPreferences prefs=activity.getSharedPreferences("user_state", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_state", user_state);
		editor.commit();
	}
	public String get_user_state(){
		SharedPreferences prefs =activity.getSharedPreferences("user_state",activity.MODE_PRIVATE);
		String user_state=prefs.getString("user_state", null);
		return user_state;
	}

	public void set_req(String user_country){
		SharedPreferences prefs=activity.getSharedPreferences("user_country", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_country", user_country);
		editor.commit();
	}
	public String get_req(){
		SharedPreferences prefs =activity.getSharedPreferences("user_country",activity.MODE_PRIVATE);
		String user_country=prefs.getString("user_country", null);
		return user_country;
	}

	public void set_name(String user_country){
		SharedPreferences prefs=activity.getSharedPreferences("gender", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("gender", user_country);
		editor.commit();
	}
	public String get_name(){
		SharedPreferences prefs =activity.getSharedPreferences("gender",activity.MODE_PRIVATE);
		String gender=prefs.getString("gender", null);
		return gender;
	}

	public void set_user_town(String user_town){
		SharedPreferences prefs=activity.getSharedPreferences("user_town", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_town", user_town);
		editor.commit();
	}
	public String get_user_town(){
		SharedPreferences prefs =activity.getSharedPreferences("user_town",activity.MODE_PRIVATE);
		String user_town=prefs.getString("user_town", null);
		return user_town;
	}

	public void set_user_pincode(String pincode){
		SharedPreferences prefs=activity.getSharedPreferences("pincode", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("pincode", pincode);
		editor.commit();
	}
	public String get_user_pincode(){
		SharedPreferences prefs =activity.getSharedPreferences("pincode",activity.MODE_PRIVATE);
		String pincode=prefs.getString("pincode", null);
		return pincode;
	}

	public void set_user_email(String user_email){
		SharedPreferences prefs=activity.getSharedPreferences("email", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("email", user_email);
		editor.commit();
	}
	public String get_user_email(){
		SharedPreferences prefs =activity.getSharedPreferences("email",activity.MODE_PRIVATE);
		String user_email=prefs.getString("email", null);
		return user_email;
	}
	public void set_user_mid(String user_email){
		SharedPreferences prefs=activity.getSharedPreferences("mid", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("mid", user_email);
		editor.commit();
	}
	public String get_user_mid(){
		SharedPreferences prefs =activity.getSharedPreferences("mid",activity.MODE_PRIVATE);
		String user_email=prefs.getString("mid", null);
		return user_email;
	}
	public void set_userid(String id){
		SharedPreferences prefs=activity.getSharedPreferences("user_id", activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("user_id", id);
		editor.commit();
	}
	public String get_userid(){
		SharedPreferences prefs =activity.getSharedPreferences("user_id",activity.MODE_PRIVATE);
		String id=prefs.getString("user_id", null);
		return id;
	}


	public void set_user_login(int login){
		SharedPreferences prefs = activity.getSharedPreferences("login",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("login", String.valueOf(login));
		editor.commit();
	}
	public int get_user_login(){
		SharedPreferences prefs =activity.getSharedPreferences("login",activity.MODE_PRIVATE);
		int login= Integer.parseInt(prefs.getString("login", "0"));
		return login;

	}

	/////////////////////////////////////////////////
	public void set_longg(String longg){
		SharedPreferences prefs = activity.getSharedPreferences("longg",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("longg", longg);
		editor.commit();
	}
	public String get_longg(){
		SharedPreferences prefs =activity.getSharedPreferences("longg",activity.MODE_PRIVATE);
		String longg= prefs.getString("longg", null);
		return longg;

	}
	public void set_lat(String lat){
		SharedPreferences prefs = activity.getSharedPreferences("lat",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("lat", lat);
		editor.commit();
	}
	public String get_lat(){
		SharedPreferences prefs =activity.getSharedPreferences("lat",activity.MODE_PRIVATE);
		String lat= prefs.getString("lat", null);
		return lat;

	}
	public void set_longg1(String longg){
		SharedPreferences prefs = activity.getSharedPreferences("lon",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("lon", longg);
		editor.commit();
	}
	public String get_longg1(){
		SharedPreferences prefs =activity.getSharedPreferences("lon",activity.MODE_PRIVATE);
		String longg= prefs.getString("lon", null);
		return longg;

	}
	public void set_lat1(String lat){
		SharedPreferences prefs = activity.getSharedPreferences("latt",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("latt", lat);
		editor.commit();
	}
	public String get_lat1(){
		SharedPreferences prefs =activity.getSharedPreferences("latt",activity.MODE_PRIVATE);
		String lat= prefs.getString("latt", null);
		return lat;

	}
	public void set_order(String order){
		SharedPreferences prefs = activity.getSharedPreferences("order",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("order", String.valueOf(order));
		editor.commit();
	}
	public String get_order(){
		SharedPreferences prefs =activity.getSharedPreferences("order",activity.MODE_PRIVATE);
		String order= prefs.getString("order", null);
		return order;

	}
	public void set_catid(String catid){
		SharedPreferences prefs = activity.getSharedPreferences("catid",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("catid", String.valueOf(catid));
		editor.commit();
	}
	public String get_catid(){
		SharedPreferences prefs =activity.getSharedPreferences("catid",activity.MODE_PRIVATE);
		String catid= prefs.getString("catid", null);
		return catid;

	}
	public void set_proid(String proid){
		SharedPreferences prefs = activity.getSharedPreferences("proid",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("proid", String.valueOf(proid));
		editor.commit();
	}
	public String get_proid(){
		SharedPreferences prefs =activity.getSharedPreferences("proid",activity.MODE_PRIVATE);
		String proid= prefs.getString("proid", null);
		return proid;

	}

	public void set_MobToken(String MobToken){
		SharedPreferences prefs = activity.getSharedPreferences("MobToken",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("MobToken", String.valueOf(MobToken));
		editor.commit();
	}
	public String get_MobToken(){
		SharedPreferences prefs =activity.getSharedPreferences("MobToken",activity.MODE_PRIVATE);
		String MobToken= prefs.getString("MobToken", null);
		return MobToken;

	}

	public void set_Token(String Token){
		SharedPreferences prefs = activity.getSharedPreferences("Token",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("Token", String.valueOf(Token));
		editor.commit();
	}
	public String get_Token(){
		SharedPreferences prefs =activity.getSharedPreferences("Token",activity.MODE_PRIVATE);
		String Token= prefs.getString("Token", null);
		return Token;

	}

	public void set_TokenId(String TokenId){
		SharedPreferences prefs = activity.getSharedPreferences("TokenId",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("TokenId", String.valueOf(TokenId));
		editor.commit();
	}
	public String get_TokenId(){
		SharedPreferences prefs =activity.getSharedPreferences("TokenId",activity.MODE_PRIVATE);
		String TokenId= prefs.getString("TokenId", null);
		return TokenId;

	}

	public void set_RefBy(String initmateplace){
		SharedPreferences prefs = activity.getSharedPreferences("initmateplace",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("initmateplace", String.valueOf(initmateplace));
		editor.commit();
	}
	public String get_RefBy(){
		SharedPreferences prefs =activity.getSharedPreferences("initmateplace",activity.MODE_PRIVATE);
		String initmateplace= prefs.getString("initmateplace", null);
		return initmateplace;

	}

	public void set_RefCode(String initmatedob){
		SharedPreferences prefs = activity.getSharedPreferences("refcode",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("refcode", String.valueOf(initmatedob));
		editor.commit();
	}
	public String get_RefCode(){
		SharedPreferences prefs =activity.getSharedPreferences("refcode",activity.MODE_PRIVATE);
		String initmatedob= prefs.getString("refcode", null);
		return initmatedob;

	}

	public void set_initmate(String initmate){
		SharedPreferences prefs = activity.getSharedPreferences("initmate",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("initmate", String.valueOf(initmate));
		editor.commit();
	}
	public String get_initmate(){
		SharedPreferences prefs =activity.getSharedPreferences("initmate",activity.MODE_PRIVATE);
		String initmate= prefs.getString("initmate", null);
		return initmate;

	}
	public void set_spinner(String spinner){
		SharedPreferences prefs = activity.getSharedPreferences("spinner",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("spinner", String.valueOf(spinner));
		editor.commit();
	}
	public String get_spinner(){
		SharedPreferences prefs =activity.getSharedPreferences("spinner",activity.MODE_PRIVATE);
		String spinner= prefs.getString("spinner", null);
		return spinner;

	}
	public void set_initmatetime(String initmatetime){
		SharedPreferences prefs = activity.getSharedPreferences("initmatetime",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("initmatetime", String.valueOf(initmatetime));
		editor.commit();
	}
	public String get_initmatetime(){
		SharedPreferences prefs =activity.getSharedPreferences("initmatetime",activity.MODE_PRIVATE);
		String initmatetime= prefs.getString("initmatetime", null);
		return initmatetime;

	}


	public void set_state_namee(String state_namee){
		SharedPreferences prefs = activity.getSharedPreferences("state_namee",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("state_namee", state_namee);
		editor.commit();
	}
	public String get_state_namee(){
		SharedPreferences prefs =activity.getSharedPreferences("state_namee",activity.MODE_PRIVATE);
		String state_namee= prefs.getString("state_namee", null);
		return state_namee;

	}

	public void set_city_namee(String city_namee){
		SharedPreferences prefs = activity.getSharedPreferences("city_namee",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("city_namee", city_namee);
		editor.commit();
	}
	public String get_city_namee(){
		SharedPreferences prefs =activity.getSharedPreferences("city_namee",activity.MODE_PRIVATE);
		String city_namee= prefs.getString("city_namee", null);
		return city_namee;

	}

	public void set_street_namee(String street_namee){
		SharedPreferences prefs = activity.getSharedPreferences("street_namee",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("street_namee", street_namee);
		editor.commit();
	}
	public String get_street_namee(){
		SharedPreferences prefs =activity.getSharedPreferences("street_namee",activity.MODE_PRIVATE);
		String street_namee= prefs.getString("street_namee", null);
		return street_namee;

	}

	public void set_pass(String reg_company_passs){
		SharedPreferences prefs = activity.getSharedPreferences("reg_company_passs",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("reg_company_passs", reg_company_passs);
		editor.commit();
	}
	public String get_pass(){
		SharedPreferences prefs =activity.getSharedPreferences("reg_company_passs",activity.MODE_PRIVATE);
		String reg_company_passs= prefs.getString("reg_company_passs", null);
		return reg_company_passs;

	}

	public void set_reg_company_phonee(String reg_company_phonee){
		SharedPreferences prefs = activity.getSharedPreferences("reg_company_phonee",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("reg_company_phonee", reg_company_phonee);
		editor.commit();
	}
	public String get_reg_company_phonee(){
		SharedPreferences prefs =activity.getSharedPreferences("reg_company_phonee",activity.MODE_PRIVATE);
		String reg_company_phonee= prefs.getString("reg_company_phonee", null);
		return reg_company_phonee;

	}

	public void set_reg_company_ownerr(String reg_company_ownerr){
		SharedPreferences prefs = activity.getSharedPreferences("reg_company_ownerr",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("reg_company_ownerr", reg_company_ownerr);
		editor.commit();
	}
	public String get_reg_company_ownerr(){
		SharedPreferences prefs =activity.getSharedPreferences("reg_company_ownerr",activity.MODE_PRIVATE);
		String reg_company_ownerr= prefs.getString("reg_company_ownerr", null);
		return reg_company_ownerr;

	}

	public void set_reg_company_namee(String reg_company_namee){
		SharedPreferences prefs = activity.getSharedPreferences("reg_company_namee",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("reg_company_namee", reg_company_namee);
		editor.commit();
	}
	public String get_reg_company_namee(){
		SharedPreferences prefs =activity.getSharedPreferences("reg_company_namee",activity.MODE_PRIVATE);
		String reg_company_namee= prefs.getString("reg_company_namee", null);
		return reg_company_namee;

	}

	public void set_reg_company_about(String reg_company_about){
		SharedPreferences prefs = activity.getSharedPreferences("reg_company_about",activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=prefs.edit();
		editor.putString("reg_company_about", reg_company_about);
		editor.commit();
	}
	public String get_reg_company_about(){
		SharedPreferences prefs =activity.getSharedPreferences("reg_company_about",activity.MODE_PRIVATE);
		String reg_company_about= prefs.getString("reg_company_about", null);
		return reg_company_about;

	}
}



