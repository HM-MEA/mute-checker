package jp.dip.jimanglaurant;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CronServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(CronServlet.class.getName());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		EntityManager em = EMF.get().createEntityManager();
		try{
			Query query = em.createNamedQuery("getCronTimeStamp");
			List<CronTimeStamp> clist = (List<CronTimeStamp>) query.getResultList(); //前回Cronが実行された時間を取得
			CronTimeStamp cts;
			if(clist.isEmpty()){ //時間が空であればそれを格納
				cts = new CronTimeStamp(DateUtils.getFormatedNowDate());
				em.persist(cts);
			}else{
				cts = clist.get(0);
				Calendar nowcalendar = DateUtils.getParsedCalendar(DateUtils.getFormatedNowDate()); //現在時間のCalendar
				Calendar oldcalendar = DateUtils.getParsedCalendar(cts.getCron_execute_at()); //前回のCron実行時間のCalendar
								
				ArrayList<UserAccount> list;
				String taskurl = "";
				if(nowcalendar.get(Calendar.DAY_OF_MONTH) == oldcalendar.get(Calendar.DAY_OF_MONTH)){ //その日初めてではないCronの実行の場合
					Query q = em.createNamedQuery("getUserAccountByCompletedFlag");
					q.setParameter("cf", false);
					list = new ArrayList<>(q.getResultList()); //処理が終了していないアカウントを呼び出す
					taskurl = "/task";	//定時処理用URL
					
				}else{ //その日初めての実行の場合
					Query q = em.createNamedQuery("getAllUserAccount");
					list = new ArrayList<>(q.getResultList()); //すべてのアカウントを呼び出す
					taskurl = "/task_init"; //日の最初の初期化定時処理用URL
				}
				
				Queue queue = QueueFactory.getQueue("task");
				for (UserAccount ua : list) {
					TaskOptions to = TaskOptions.Builder.withUrl(taskurl).param("user_id",String.valueOf(ua.getUser_id()));
					queue.add(to); //TaskQueueに放り込む
				}
				
				cts.setCron_execute_at(DateUtils.getFormatedNowDate());
				em.merge(cts);
			}
		} catch (ParseException e) {
			throw new ServletException(e);
		} finally {
			em.close();
		}
	}
}
