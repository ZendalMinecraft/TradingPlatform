/*
 * Copyright (c) Alexander <gasfull98@gmail.com> Chapchuk
 * Project name: TradingPlatform
 *
 * Licensed under the MIT License. See LICENSE file in the project root for license information.
 */

package ru.zendal.socket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.scalecube.socketio.Session;
import io.scalecube.socketio.SocketIOListener;
import org.bson.Document;
import ru.zendal.socket.command.Command;
import ru.zendal.socket.exception.CommandProcessSocketIOException;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class AdapterServerListener implements SocketIOListener {


    private final Charset charset;
    private final List<Session> storageSessions = new ArrayList<>();

    private final List<Command> storageCommandProcessors = new ArrayList<>();
    private final Logger logger;

    public AdapterServerListener(Charset character, Logger logger) {
        this.logger = logger;
        this.charset = character;
    }

    @Override
    public void onConnect(Session session) {
        storageSessions.add(session);
        logger.info("New connection: "
                + session.getRemoteAddress().toString()
        );
    }

    @Override
    public void onMessage(Session session, ByteBuf message) {
        Document response = new Document();
        try {
            Document data = processMessage(session, message);
            response.put("code", 0);
            response.put("response", data);
            sendMessage(session, response);
        } catch (CommandProcessSocketIOException e) {
            response.put("code", e.getErrorCode());
            response.put("errorMessage", e.getMessage());
            sendMessage(session, response);
        }
    }

    @Override
    public void onDisconnect(Session session) {
        storageSessions.remove(session);
        logger.info("Close  connection: "
                + session.getRemoteAddress().toString()
        );
    }

    public void addCommandProcessors(Command command) {
        storageCommandProcessors.add(command);
    }


    /**
     * Send message to all Sessions
     *
     * @param message Message BSON Document
     */
    public void sendMessageAllSessions(Document message) {
        for (Session session : storageSessions) {
            this.sendMessage(session, message);
        }
    }

    public void sendMessage(Session session, Document message) {
        session.send(convertStringToByteBuff(message.toJson(), charset));
    }

    /**
     * Process message from client
     *
     * @param session Session
     * @param message Byte buf message
     */
    private Document processMessage(Session session, ByteBuf message) throws CommandProcessSocketIOException {

        String text = message.toString(charset);
        Document jsonDocument = Document.parse(text);
        String commandName = jsonDocument.getString("command");
        if (commandName == null) {
            throw new CommandProcessSocketIOException(
                    "Missing required key 'command'",
                    CommandProcessSocketIOException.CODE_MISSING_REQUIRED_KEY
            );
        }

        for (Command command : storageCommandProcessors) {
            if (command.canProcess(jsonDocument)) {
                return command.process(jsonDocument);
            }
        }

        throw new CommandProcessSocketIOException(
                "Undefined command",
                CommandProcessSocketIOException.CODE_UNDEFINED_COMMAND
        );
    }


    /**
     * Convert String to ByteBuff
     *
     * @param data    String data
     * @param charset Charset data
     * @return ByteBuf
     */
    private ByteBuf convertStringToByteBuff(String data, Charset charset) {
        return Unpooled.copiedBuffer(data.toCharArray(), charset);
    }
}
