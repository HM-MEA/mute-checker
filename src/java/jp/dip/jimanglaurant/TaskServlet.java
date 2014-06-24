package jp.dip.jimanglaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Relationship;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TaskServlet extends HttpServlet {
	private static final Logger log = Logger.getLogger(TaskServlet.class.getName());

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
			ArrayList<Long> follower_list = ua.getFollower_list();
			Random rand = new Random();
			for(int i = 0;i < 50;i++){
				if(follower_list.isEmpty()){
					ua.setComplete_flag(true);
					ua.setMuted_list(ua.getTmp_muted_list());
					em.merge(ua);
					log.info("Complete");
					break;
				}

				int n = rand.nextInt(follower_list.size());
				Long id = follower_list.get(n);
				follower_list.remove(n);
				if(twitter.showUser(id).isProtected()){
					log.warning("user:" + id + " is protected");
					continue;
				}
				Relationship rs = twitter.showFriendship(id, ua.getUser_id());
				if(rs.isSourceMutingTarget()){
					ua.getTmp_muted_list().add(id);
				}
			}
			ua.setUpdated_at(DateUtils.getFormatedNowDate());
			em.merge(ua);
		} catch (TwitterException e){
			throw new ServletException(e);
		} finally {
			em.close();
		}
	}
}
