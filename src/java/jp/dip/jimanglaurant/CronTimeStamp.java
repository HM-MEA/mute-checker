package jp.dip.jimanglaurant;

import com.google.appengine.api.datastore.Key;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries({
	@NamedQuery(name="getCronTimeStamp",query="select cts from CronTimeStamp cts")
})
public class CronTimeStamp implements Serializable {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Key key;
	private String cron_execute_at;

	public CronTimeStamp(String cron_execute_at) {
		this.cron_execute_at = cron_execute_at;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getCron_execute_at() {
		return cron_execute_at;
	}

	public void setCron_execute_at(String cron_execute_at) {
		this.cron_execute_at = cron_execute_at;
	}
	
}
