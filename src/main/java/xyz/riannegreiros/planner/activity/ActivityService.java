package xyz.riannegreiros.planner.activity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.riannegreiros.planner.trip.Trip;

import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {
    
    @Autowired
    private ActivityRepository repository;
    
    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occurs_at(), trip);
        
        this.repository.save(newActivity);
        
        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesFromTripId(UUID id) {
        return this.repository.findByTripId(id).stream().map(activity -> new ActivityData(activity.getId(), activity.getTitle(), activity.getOccursAt())).toList();
    }
}
