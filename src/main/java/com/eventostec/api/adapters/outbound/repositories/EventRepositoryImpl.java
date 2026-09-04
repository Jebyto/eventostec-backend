package com.eventostec.api.adapters.outbound.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.eventostec.api.adapters.outbound.entities.JpaEventEntity;
import com.eventostec.api.domain.event.Event;
import com.eventostec.api.domain.event.EventAddressProjection;
import com.eventostec.api.domain.event.EventRepository;

@Repository
public class EventRepositoryImpl implements EventRepository {

    private final JpaEventRepository jpaEventRepository;

    public EventRepositoryImpl(JpaEventRepository jpaEventRepository) {
        this.jpaEventRepository = jpaEventRepository;
    }

    @Override
    public Event save(Event event) {
        JpaEventEntity entity = new JpaEventEntity(event);
        this.jpaEventRepository.save(entity);

        return new Event(entity.getId(), entity.getTitle(), entity.getDescription(), entity.getImgUrl(),
                entity.getEventUrl(), entity.getRemote(), entity.getDate());
    }

    @Override
    public Optional<Event> findById(UUID id) {
        Optional<JpaEventEntity> entity = jpaEventRepository.findById(id);
        return entity.map(e -> new Event(e.getId(), e.getTitle(), e.getDescription(), e.getImgUrl(), e.getEventUrl(),
                e.getRemote(), e.getDate())).orElse(null);
    }

    @Override
    public List<Event> findAll() {
        List<JpaEventEntity> entities = jpaEventRepository.findAll();
        return entities.stream()
                .map(e -> new Event(e.getId(), e.getTitle(), e.getDescription(), e.getImgUrl(), e.getEventUrl(),
                        e.getRemote(), e.getDate()))
                .toList();
    }

    @Override
    public void deleteById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Event id cannot be null.");
        }

        jpaEventRepository.deleteById(id);
    }

    @Override
    public Page<EventAddressProjection> findUpcomingEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaEventRepository.findUpcomingEvents(new Date(), pageable);
    }

    @Override
    public Page<EventAddressProjection> findFilteredEvents(String city, String uf, Date startDate, Date endDate,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return jpaEventRepository.findFilteredEvents(city, uf, startDate, endDate, pageable);
    }

    @Override
    public List<EventAddressProjection> findEventsByTitle(String title) {
        return jpaEventRepository.findEventsByTitle(title);
    }

}
