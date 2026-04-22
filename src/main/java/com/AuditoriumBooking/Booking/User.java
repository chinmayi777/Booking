package com.AuditoriumBooking.Booking;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private long id;
    private String eventName;
    private String name;
    private String department;
    private Integer sem;
    private LocalDateTime startsAt;
    private LocalDateTime endsAt;



    public User(String eventName, String name, String department, int sem, LocalDateTime startsAt, LocalDateTime endsAt) {
        this.eventName = eventName;
        this.name = name;
        this.department = department;
        this.sem = sem;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }


    public User(long id, String eventName, String name, String department, int sem, LocalDateTime startsAt, LocalDateTime endsAt) {
        this.id = id;
        this.eventName = eventName;
        this.name = name;
        this.department = department;
        this.sem = sem;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
    }


    public User(){
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSem() {
        return sem;
    }

    public void setSem(int sem) {
        this.sem = sem;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public void setStartsAt(LocalDateTime startsAt) {
        this.startsAt = startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public void setEndsAt(LocalDateTime endsAt) {
        this.endsAt = endsAt;
    }


}
