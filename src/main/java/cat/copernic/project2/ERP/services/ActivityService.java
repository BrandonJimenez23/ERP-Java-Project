package cat.copernic.project2.ERP.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import cat.copernic.project2.ERP.dao.ActivityDao;
import cat.copernic.project2.ERP.domain.Activity;

/**
 *
 * @author oscar
 */
@Service
public class ActivityService {

    private final ActivityDao activityDao;

    @Autowired
    public ActivityService(ActivityDao activitiesDao) {
        this.activityDao = activitiesDao;
    }

    public Activity saveOrUpdateActivity(Activity activity) {
        if (activity.getId() != null) {
            Activity existingActivity = activityDao.findById(activity.getId()).orElse(null);
            existingActivity.setName(activity.getName());
            existingActivity.setPlace(activity.getPlace());
            existingActivity.setStartDate(activity.getStartDate());
            existingActivity.setEndDate(activity.getEndDate());
            existingActivity.setIsFree(activity.isIsFree());
            existingActivity.setParticipantLimit(activity.getParticipantLimit());
            existingActivity.setPricePerPerson(activity.getPricePerPerson());
            existingActivity.setNumberOfPayments(activity.getNumberOfPayments());
            return activityDao.save(existingActivity);
        }
        return activityDao.save(activity);
    }

    public void deleteActivity(Long id) {
        activityDao.deleteById(id);
    }

    public Activity findById(Long id) {
        return activityDao.findById(id).orElse(null);
    }

    public List<Activity> getAllActivities() {
        return activityDao.findAll();
    }

    public List<Activity> findActivityById(List<Long> id) {
        return activityDao.findAllById(id);
    }
}
