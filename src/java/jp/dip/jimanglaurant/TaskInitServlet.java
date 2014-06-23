package jp.dip.jimanglaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TaskInitServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(TaskInitServlet.class.getName());

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		long user_id = Long.parseLong(request.getParameter("user_id"));
		EntityManager em  = EMF.get().createEntityManager();
		Query query = em.createNamedQuery("getUserAccountByUserId");
		query.setParameter("uid", user_id);
		UserAccount ua = (UserAccount) query.getResultList().get(0);
		log.info("user:" + ua.getScreen_name());
		
		TwitterFactory tf = new TwitterFactory();
		Twitter twitter = tf.getInstance();
		twitter.setOAuthAccessToken(new AccessToken(ua.getAccess_token(),ua.getAccess_token_secret()));
		
		try{
			ArrayList<Long> follower_list = new ArrayList<>();
			IDs ids = twitter.getFollowersIDs(IDs.START);
			long id[] = ids.getIDs();
			for(int i = 0;i < id.length;i++){
				follower_list.add(id[i]);
			}
			while(ids.hasNext()){
				ids = twitter.getFollowersIDs(ids.getNextCursor());
				id = ids.getIDs();
				for(int i = 0;i < id.length;i++){
				follower_list.add(id[i]);
				}
			}
			
			ua.setFollower_list(follower_list);
			ua.setTmp_muted_list(new ArrayList<Long>());
			ua.setUpdated_at(DateUtils.getFormatedNowDate());
			ua.setComplete_flag(false);
			
			em.merge(ua);
		} catch (TwitterException e){
			throw new ServletException(e);
		} finally {
			em.close();
		}
	}
}
