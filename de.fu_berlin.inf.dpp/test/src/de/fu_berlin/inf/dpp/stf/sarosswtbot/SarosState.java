package de.fu_berlin.inf.dpp.stf.sarosswtbot;

import java.rmi.RemoteException;
import java.util.List;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.eclipse.swtbot.swt.finder.utils.FileUtils;
import org.jivesoftware.smack.Roster;
import org.limewire.collection.Tuple;
import org.osgi.framework.Bundle;

import de.fu_berlin.inf.dpp.Saros;
import de.fu_berlin.inf.dpp.User;
import de.fu_berlin.inf.dpp.editor.EditorManager;
import de.fu_berlin.inf.dpp.net.ConnectionState;
import de.fu_berlin.inf.dpp.net.JID;
import de.fu_berlin.inf.dpp.net.internal.DataTransferManager;
import de.fu_berlin.inf.dpp.net.internal.DataTransferManager.NetTransferMode;
import de.fu_berlin.inf.dpp.project.ISarosSession;
import de.fu_berlin.inf.dpp.project.SessionManager;

/**
 * The goal of this class is to gather state and provide an RMI interface for
 * getting internal states from the outside.
 */
public class SarosState implements ISarosState {

    private transient static final Logger log = Logger
        .getLogger(SarosState.class);

    public static SarosState classVariable;

    public SarosState() {
        // Default constructor needed for RMI
    }

    public SarosState(Saros saros, SessionManager sessionManager,
        DataTransferManager dataTransferManager, EditorManager editorManager) {
        this.saros = saros;
        this.sessionManager = sessionManager;
        this.dataTransferManager = dataTransferManager;
        this.editorManager = editorManager;
    }

    protected transient Saros saros;

    protected transient SessionManager sessionManager;

    protected transient DataTransferManager dataTransferManager;

    protected transient EditorManager editorManager;

    public boolean areDrivers(List<JID> jids) {
        boolean result = true;
        for (JID jid : jids) {
            try {
                ISarosSession sarosSession = sessionManager.getSarosSession();
                User user = sarosSession.getUser(jid);
                result &= sarosSession.getDrivers().contains(user);
            } catch (Exception e) {
                return false;
            }
        }
        return result;
    }

    public boolean areObservers(List<JID> jids) {
        boolean result = true;
        for (JID jid : jids) {
            try {
                ISarosSession sarosSession = sessionManager.getSarosSession();
                User user = sarosSession.getUser(jid);
                result &= sarosSession.getObservers().contains(user);
            } catch (Exception e) {
                return false;
            }
        }
        return result;
    }

    public boolean existSession() {
        return sessionManager.getSarosSession() != null;
    }

    public boolean areParticipants(List<JID> jids) {
        boolean result = true;
        for (JID jid : jids) {
            try {
                ISarosSession sarosSession = sessionManager.getSarosSession();
                result &= sarosSession.getParticipants().contains(
                    sarosSession.getUser(jid));
            } catch (Exception e) {
                return false;
            }
        }
        return result;
    }

    public Tuple<NetTransferMode, NetTransferMode> getConnection(JID destJid) {
        NetTransferMode outgoingMode = dataTransferManager
            .getTransferMode(destJid);
        NetTransferMode incomingMode = dataTransferManager
            .getTransferMode(destJid);
        return new Tuple<NetTransferMode, NetTransferMode>(incomingMode,
            outgoingMode);
    }

    public ConnectionState getXmppConnectionState() {
        return saros.getConnectionState();
    }

    public boolean hasContactWith(JID jid) throws RemoteException {
        Roster roster = saros.getRoster();
        String jidBase = jid.getBase();
        return roster.contains(jidBase);
    }

    public boolean isConnectedByXMPP() {
        return saros.isConnected();
    }

    public boolean isFollowing() throws RemoteException {
        return editorManager.isFollowing();
    }

    public boolean isDriver(JID jid) throws RemoteException {
        ISarosSession sarosSession = sessionManager.getSarosSession();
        User user = sarosSession.getUser(jid);
        log.debug("isDriver(" + jid.toString() + ") == "
            + sarosSession.getDrivers().contains(user));
        return sarosSession.getDrivers().contains(user);
    }

    public boolean isIncomingConnectionIBB(JID destJid) throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.IBB);
    }

    public boolean isIncomingConnectionJingleTCP(JID destJid)
        throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.JINGLETCP);
    }

    public boolean isIncomingConnectionJingleUDP(JID destJid)
        throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.JINGLEUDP);
    }

    public boolean isIncomingConnectionSocks5ByteStream(JID destJid)
        throws RemoteException {
        throw new NotImplementedException(
            "We can not get NetTransferMode Socks5ByteStream connection in Saros yet.");
    }

    public boolean isObserver(JID jid) throws RemoteException {
        ISarosSession sarosSession = sessionManager.getSarosSession();
        User user = sarosSession.getUser(jid);
        log.debug("isObserver(" + jid.toString() + ") == "
            + sarosSession.getObservers().contains(user));
        return sarosSession.getObservers().contains(user);
    }

    public boolean isOutgoingConnectionIBB(JID destJid) throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.IBB);
    }

    public boolean isOutgoingConnectionJingleTCP(JID destJid)
        throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.JINGLETCP);
    }

    public boolean isOutgoingConnectionJingleUDP(JID destJid)
        throws RemoteException {
        return dataTransferManager.getTransferMode(destJid).equals(
            NetTransferMode.JINGLEUDP);
    }

    public boolean isOutgoingConnectionSocks5ByteStream(JID destJid)
        throws RemoteException {
        throw new NotImplementedException(
            "We can not get NetTransferMode Socks5ByteStream connection in Saros yet.");
    }

    public boolean isParticipant(JID jid) throws RemoteException {
        try {
            ISarosSession sarosSession = sessionManager.getSarosSession();
            log.debug("isParticipant("
                + jid.toString()
                + ") == "
                + sarosSession.getParticipants().contains(
                    sarosSession.getUser(jid)));
            return sarosSession.getParticipants().contains(
                sarosSession.getUser(jid));
        } catch (Exception e) {
            return false;
        }
    }

    public ISarosSession getProject() throws RemoteException {
        return sessionManager.getSarosSession();
    }

    public String getContents(String path) throws RemoteException {
        Bundle bundle = saros.getBundle();
        String contents;
        try {
            contents = FileUtils.read(bundle.getEntry(path));
        } catch (NullPointerException e) {
            throw new RuntimeException("Could not open " + path);
        }
        return contents;
    }

    public String getPathToScreenShot() throws RemoteException {
        Bundle bundle = saros.getBundle();
        return bundle.getLocation().substring(16)
            + BotConfiguration.SCREENSHOTDIR;
    }

}
