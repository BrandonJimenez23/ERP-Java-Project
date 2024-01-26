package erp.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import erp.dao.ActivityDao;
import erp.domain.Activity;
import java.util.Date;

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
            existingActivity.setIsFree(activity.getIsFree());
            existingActivity.setIsLimited(activity.getIsLimited());
            existingActivity.setResources(activity.getResources());
            existingActivity.setRequirements(activity.getRequirements());
            if (existingActivity.getIsLimited()) {
                existingActivity.setParticipantLimit(activity.getParticipantLimit());
            } else {
                existingActivity.setParticipantLimit(0);
            }
            if (existingActivity.getIsFree()) {
                existingActivity.setPricePerPerson(activity.getPricePerPerson());
                existingActivity.setNumberOfPayments(activity.getNumberOfPayments());
            } else {
                existingActivity.setPricePerPerson(0);
                existingActivity.setNumberOfPayments(0);
            }

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

    public Activity findActivityById(Long id) {
        return activityDao.findById(id).orElse(null);
    }

    public boolean validateDates(Date startDate, Date endDate) {
        return startDate.compareTo(endDate) <= 0;
    }

    public boolean validatePaymentValues(double pricePerPerson, int numberOfPayments) {
        return !(pricePerPerson < 0 || numberOfPayments < 0);
    }

    public boolean validateParticipantLimit(int participantLimit) {
        return !(participantLimit < 1);
    }
}
