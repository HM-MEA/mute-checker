package jp.dip.jimanglaurant;

import com.google.appengine.api.datastore.Key;
import java.io.Serializable;
import java.util.ArrayList;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="getUserAccountByUserId",query="select ua from UserAccount ua where ua.user_id = :uid"),
	@NamedQuery(name="getUserAccountByCompletedFlag",query="select ua from UserAccount ua where ua.complete_flag = :cf"),
	@NamedQuery(name="getAllUserAccount",query="select ua from UserAccount ua")
})
public class UserAccount implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
    private long user_id;
    private String screen_name;
    private String access_token;
    private String access_token_secret;
    private ArrayList<Long> follower_list;
    private ArrayList<Long> muted_list;
    private ArrayList<Long> tmp_muted_list;
    private boolean complete_flag;
    private String updated_at;

	public UserAccount() {
	}

    public UserAccount(long user_id, String screen_name, String access_token, String access_token_secret, ArrayList<Long> follower_list, ArrayList<Long> muted_list, ArrayList<Long> tmp_muted_list, boolean complete_flag, String updated_at) {
	this.user_id = user_id;
	this.screen_name = screen_name;
	this.access_token = access_token;
	this.access_token_secret = access_token_secret;
	this.follower_list = follower_list;
	this.muted_list = muted_list;
	this.tmp_muted_list = tmp_muted_list;
	this.complete_flag = complete_flag;
	this.updated_at = updated_at;
    }

    public Key getKey() {
	return key;
    }

    public void setKey(Key key) {
	this.key = key;
    }

    public long getUser_id() {
	return user_id;
    }

    public void setUser_id(long user_id) {
	this.user_id = user_id;
    }

    public String getScreen_name() {
	return screen_name;
    }

    public void setScreen_name(String screen_name) {
	this.screen_name = screen_name;
    }

    public String getAccess_token() {
	return access_token;
    }

    public void setAccess_token(String access_token) {
	this.access_token = access_token;
    }

    public String getAccess_token_secret() {
	return access_token_secret;
    }

    public void setAccess_token_secret(String access_token_secret) {
	this.access_token_secret = access_token_secret;
    }

    public ArrayList<Long> getFollower_list() {
	return follower_list;
    }

    public void setFollower_list(ArrayList<Long> follower_list) {
	this.follower_list = follower_list;
    }

    public ArrayList<Long> getMuted_list() {
	return muted_list;
    }

    public void setMuted_list(ArrayList<Long> muted_list) {
	this.muted_list = muted_list;
    }

    public ArrayList<Long> getTmp_muted_list() {
	return tmp_muted_list;
    }

    public void setTmp_muted_list(ArrayList<Long> tmp_muted_list) {
	this.tmp_muted_list = tmp_muted_list;
    }

    public boolean isComplete_flag() {
	return complete_flag;
    }

    public void setComplete_flag(boolean complete_flag) {
	this.complete_flag = complete_flag;
    }  

    public String getUpdated_at() {
	return updated_at;
    }

    public void setUpdated_at(String updated_at) {
	this.updated_at = updated_at;
    }
}
