package com.noscompany.snake.game.online.server.available.rooms.commons;

import lombok.AllArgsConstructor;
import org.atmosphere.cpr.*;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor
class BroadcasterMock implements Broadcaster {
    private String id;

    @Override
    public Broadcaster initialize(String name, URI uri, AtmosphereConfig config) {
        return this;
    }

    @Override
    public void setSuspendPolicy(long maxSuspended, POLICY policy) {

    }

    @Override
    public Future<Object> broadcast(Object o) {
        return null;
    }

    @Override
    public Future<Object> delayBroadcast(Object o) {
        return null;
    }

    @Override
    public Future<Object> delayBroadcast(Object o, long delay, TimeUnit t) {
        return null;
    }

    @Override
    public Future<Object> scheduleFixedBroadcast(Object o, long period, TimeUnit t) {
        return null;
    }

    @Override
    public Future<Object> scheduleFixedBroadcast(Object o, long waitFor, long period, TimeUnit t) {
        return null;
    }

    @Override
    public Future<Object> broadcast(Object o, AtmosphereResource resource) {
        return null;
    }

    @Override
    public Future<Object> broadcastOnResume(Object o) {
        return null;
    }

    @Override
    public Future<Object> broadcast(Object o, Set<AtmosphereResource> subset) {
        return null;
    }

    @Override
    public Broadcaster addAtmosphereResource(AtmosphereResource resource) {
        return null;
    }

    @Override
    public Broadcaster removeAtmosphereResource(AtmosphereResource resource) {
        return null;
    }

    @Override
    public void setBroadcasterConfig(BroadcasterConfig bc) {

    }

    @Override
    public BroadcasterConfig getBroadcasterConfig() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public Collection<AtmosphereResource> getAtmosphereResources() {
        return new LinkedList<>();
    }

    @Override
    public void setScope(SCOPE scope) {

    }

    @Override
    public SCOPE getScope() {
        return null;
    }

    @Override
    public void setID(String name) {
        this.id = name;
    }

    @Override
    public String getID() {
        return id;
    }

    @Override
    public void resumeAll() {

    }

    @Override
    public void releaseExternalResources() {

    }

    @Override
    public void setBroadcasterLifeCyclePolicy(BroadcasterLifeCyclePolicy policy) {

    }

    @Override
    public BroadcasterLifeCyclePolicy getBroadcasterLifeCyclePolicy() {
        return null;
    }

    @Override
    public void addBroadcasterLifeCyclePolicyListener(BroadcasterLifeCyclePolicyListener b) {

    }

    @Override
    public void removeBroadcasterLifeCyclePolicyListener(BroadcasterLifeCyclePolicyListener b) {

    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public Future<Object> awaitAndBroadcast(Object t, long time, TimeUnit timeUnit) {
        return null;
    }

    @Override
    public Broadcaster addBroadcasterListener(BroadcasterListener b) {
        return null;
    }

    @Override
    public Broadcaster removeBroadcasterListener(BroadcasterListener b) {
        return null;
    }
}