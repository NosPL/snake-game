package com.noscompany.snake.game.online.host.server.message.handler.commons;

import lombok.RequiredArgsConstructor;
import org.atmosphere.cpr.*;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static lombok.AccessLevel.PRIVATE;

@RequiredArgsConstructor(access = PRIVATE)
class AtmosphereResourceMock implements AtmosphereResource {
    private final String id;
    private final LinkedList<String> serializedMessages = new LinkedList<>();

    public static AtmosphereResourceMock create(String id) {
        return new AtmosphereResourceMock(id);
    }

    public Messages getMessages() {
        return new Messages(new LinkedList<>(serializedMessages));
    }

    @Override
    public TRANSPORT transport() {
        return null;
    }

    @Override
    public AtmosphereResource resumeOnBroadcast(boolean resumeOnBroadcast) {
        return this;
    }

    @Override
    public boolean isSuspended() {
        return false;
    }

    @Override
    public boolean resumeOnBroadcast() {
        return false;
    }

    @Override
    public boolean isResumed() {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public AtmosphereResource resume() {
        return this;
    }

    @Override
    public AtmosphereResource suspend() {
        return this;
    }

    @Override
    public AtmosphereResource suspend(long timeout) {
        return this;
    }

    @Override
    public AtmosphereResource suspend(long timeout, TimeUnit timeunit) {
        return this;
    }

    @Override
    public AtmosphereRequest getRequest() {
        return null;
    }

    @Override
    public AtmosphereResponse getResponse() {
        return null;
    }

    @Override
    public AtmosphereConfig getAtmosphereConfig() {
        return null;
    }

    @Override
    public Broadcaster getBroadcaster() {
        return null;
    }

    @Override
    public List<Broadcaster> broadcasters() {
        return null;
    }

    @Override
    public AtmosphereResource removeFromAllBroadcasters() {
        return null;
    }

    @Override
    public AtmosphereResource setBroadcaster(Broadcaster broadcaster) {
        return null;
    }

    @Override
    public AtmosphereResource addBroadcaster(Broadcaster broadcaster) {
        return null;
    }

    @Override
    public AtmosphereResource removeBroadcaster(Broadcaster broadcaster) {
        return null;
    }

    @Override
    public AtmosphereResource setSerializer(Serializer s) {
        return null;
    }

    @Override
    public AtmosphereResource write(String s) {
        serializedMessages.addLast(s);
        return this;
    }

    @Override
    public AtmosphereResource write(byte[] s) {
        return this;
    }

    @Override
    public Serializer getSerializer() {
        return null;
    }

    @Override
    public AtmosphereResourceEvent getAtmosphereResourceEvent() {
        return null;
    }

    @Override
    public AtmosphereHandler getAtmosphereHandler() {
        return null;
    }

    @Override
    public AtmosphereResource writeOnTimeout(Object o) {
        return null;
    }

    @Override
    public Object writeOnTimeout() {
        return null;
    }

    @Override
    public String uuid() {
        return id;
    }

    @Override
    public AtmosphereResource addEventListener(AtmosphereResourceEventListener e) {
        return null;
    }

    @Override
    public AtmosphereResource removeEventListener(AtmosphereResourceEventListener e) {
        return null;
    }

    @Override
    public AtmosphereResource removeEventListeners() {
        return null;
    }

    @Override
    public AtmosphereResource notifyListeners(AtmosphereResourceEvent e) {
        return null;
    }

    @Override
    public AtmosphereResource notifyListeners() {
        return null;
    }

    @Override
    public HttpSession session() {
        return null;
    }

    @Override
    public HttpSession session(boolean create) {
        return null;
    }

    @Override
    public void close() throws IOException {

    }

    @Override
    public AtmosphereResource forceBinaryWrite(boolean force) {
        return null;
    }

    @Override
    public boolean forceBinaryWrite() {
        return false;
    }

    @Override
    public AtmosphereResource initialize(AtmosphereConfig config, Broadcaster broadcaster, AtmosphereRequest req, AtmosphereResponse response, AsyncSupport asyncSupport, AtmosphereHandler atmosphereHandler) {
        return null;
    }
}
