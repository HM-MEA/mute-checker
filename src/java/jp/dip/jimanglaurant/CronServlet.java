package jp.dip.jimanglaurant;

import java.io.IOException;
import java.text.ParseException;
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
			List<CronTimeStamp> list = (List<CronTimeStamp>) query.getResultList();
			CronTimeStamp cts;
			if(list.isEmpty()){
				cts = new CronTimeStamp(DateUtils.getFormatedNowDate());
				em.persist(cts);
			}else{
				cts = list.get(0);
				Calendar nowcalendar = DateUtils.getParsedCalendar(DateUtils.getFormatedNowDate());
				Calendar oldcalendar = DateUtils.getParsedCalendar(cts.getCron_execute_at());
				if(nowcalendar.get(Calendar.DAY_OF_MONTH) == oldcalendar.get(Calendar.DAY_OF_MONTH)){
					log.info(0 + "");
				}else{
					log.info(1 + "");
				}
				
				cts.setCron_execute_at(DateUtils.getFormatedNowDate());
				em.merge(cts);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
}
