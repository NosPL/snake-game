package com.noscompany.snake.game.online.host.server.available.rooms.commons;

import lombok.Getter;
import org.atmosphere.cpr.*;

import java.util.Collection;
import java.util.LinkedList;

@Getter
class BroadcasterFactoryMock implements BroadcasterFactory {
    private final LinkedList<BroadcasterMock> broadcasterMocks = new LinkedList<>();

    @Override
    public void configure(Class<? extends Broadcaster> clazz, String broadcasterLifeCyclePolicy, AtmosphereConfig c) {
    }

    public void add(BroadcasterMock broadcasterMock) {
        broadcasterMocks.add(broadcasterMock);
    }

    @Override
    public Broadcaster get() {
        return broadcasterMocks
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Broadcaster get(Object id) {
        return broadcasterMocks
                .stream()
                .filter(broadcaster -> broadcaster.getID().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public <T extends Broadcaster> T get(Class<T> c, Object id) {
        return null;
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean add(Broadcaster b, Object id) {
        return false;
    }

    @Override
    public boolean remove(Broadcaster b, Object id) {
        return false;
    }

    @Override
    public <T extends Broadcaster> T lookup(Class<T> c, Object id) {
        return null;
    }

    @Override
    public <T extends Broadcaster> T lookup(Class<T> c, Object id, boolean createIfNull) {
        return null;
    }

    @Override
    public <T extends Broadcaster> T lookup(Object id) {
        return null;
    }

    @Override
    public <T extends Broadcaster> T lookup(Object id, boolean createIfNull) {
        return null;
    }

    @Override
    public void removeAllAtmosphereResource(AtmosphereResource r) {
    }

    @Override
    public boolean remove(Object id) {
        return false;
    }

    @Override
    public Collection<Broadcaster> lookupAll() {
        return new LinkedList<>(broadcasterMocks);
    }

    @Override
    public BroadcasterFactory addBroadcasterListener(BroadcasterListener b) {
        return this;
    }

    @Override
    public BroadcasterFactory removeBroadcasterListener(BroadcasterListener b) {
        return this;
    }

    @Override
    public Collection<BroadcasterListener> broadcasterListeners() {
        return new LinkedList<>();
    }
}
