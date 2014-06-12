package jp.dip.jimanglaurant;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.auth.RequestToken;

public class CallbackServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(CallbackServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Twitter twitter = (Twitter) request.getSession().getAttribute("twitter");
        RequestToken requestToken = (RequestToken) request.getSession().getAttribute("requestToken");
        String verifier = request.getParameter("oauth_verifier");
        try {
            twitter.getOAuthAccessToken(requestToken, verifier);
            request.getSession().removeAttribute("requestToken");
        } catch (TwitterException e) {
            throw new ServletException(e);
        }
        
	EntityManager em = EMF.get().createEntityManager();
	try{
	    Query query = em.createNamedQuery("getUserAccountByUserId");
	    query.setParameter("uid", twitter.getId());
	    List<UserAccount> list = (List<UserAccount>) query.getResultList();
	    UserAccount useraccount;
	    if(list.isEmpty()){
			useraccount = new UserAccount(twitter.getId(),twitter.getScreenName(),twitter.getOAuthAccessToken().getToken(),twitter.getOAuthAccessToken().getTokenSecret(),new ArrayList<Long>(),new ArrayList<Long>(),new ArrayList<Long>(),true,DateUtils.getFormatedNowDate());
			em.persist(useraccount);
			log.info(useraccount.getScreen_name()+"が登録されました");
        }else{
	    	useraccount = list.get(0);
            useraccount.setUser_id(twitter.getId());
            useraccount.setScreen_name(twitter.getScreenName());
            useraccount.setAccess_token(twitter.getOAuthAccessToken().getToken());
            useraccount.setAccess_token_secret(twitter.getOAuthAccessToken().getTokenSecret());
            em.merge(useraccount);
        }
	} catch (TwitterException | IllegalStateException e) {
	    e.printStackTrace();
	} finally {
	    em.close();
	}
        request.getSession().setAttribute("useraccount",twitter);    
        response.sendRedirect(request.getContextPath() + "/index.jsp");        
    }
}
