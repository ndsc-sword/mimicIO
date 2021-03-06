package org.ndsc.mimicIO.elements.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.ndsc.mimicIO.dicovery.LLDPEventHandler;
import org.ndsc.mimicIO.elements.datapath.Switch;
import org.ndsc.mimicIO.elements.port.Port;
import org.ndsc.mimicIO.elements.link.Link;
import org.ndsc.mimicIO.io.OVXSendMsg;
import org.openflow.util.HexString;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by cgz on 12/27/16.
 */
public abstract class Network<T1 extends Switch, T2 extends Port, T3 extends Link>
        implements LLDPEventHandler, OVXSendMsg {

    @SerializedName("switches")
    @Expose
    protected final Set<T1> switchSet;
    @SerializedName("links")
    @Expose
    protected final Set<T3> linkSet;
    protected final Map<Long, T1> dpidMap;
    protected final Map<T2, T2> neighborPortMap;
    protected final Map<T1, HashSet<T1>> neighborMap;

    /**
     * Instantiates the network.
     */
    protected Network() {
        this.switchSet = new HashSet<T1>();
        this.linkSet = new HashSet<T3>();
        this.dpidMap = new HashMap<Long, T1>();
        this.neighborPortMap = new HashMap<T2, T2>();
        this.neighborMap = new HashMap<T1, HashSet<T1>>();
    }

    // Protected methods to update topology (only allowed from subclasses)

    /**
     * Adds link to topology data structures.
     *
     * @param link the link
     */
    @SuppressWarnings("unchecked")
    protected void addLink(final T3 link) {
        // Actual link creation is in child classes, because creation of generic
        // types sucks
        this.linkSet.add(link);
        final T1 srcSwitch = (T1) link.getSrcSwitch();
        final T1 dstSwitch = (T1) link.getDstSwitch();
        final Port srcPort = link.getSrcPort();
        final Port dstPort = link.getSrcPort();
        srcPort.setEdge(false);
        dstPort.setEdge(false);
        final HashSet<T1> neighbours = this.neighborMap.get(srcSwitch);
        neighbours.add(dstSwitch);
        this.neighborPortMap
                .put((T2) link.getSrcPort(), (T2) link.getDstPort());
    }

    /**
     * Removes link to topology.
     *
     * @param link the link
     * @return true if successful, false otherwise
     */
    @SuppressWarnings("unchecked")
    protected boolean removeLink(final T3 link) {
        this.linkSet.remove(link);
        final T1 srcSwitch = (T1) link.getSrcSwitch();
        final T1 dstSwitch = (T1) link.getDstSwitch();
        final Port srcPort = link.getSrcPort();
        final Port dstPort = link.getSrcPort();
        srcPort.setEdge(true);
        dstPort.setEdge(true);
        final HashSet<T1> neighbours = this.neighborMap.get(srcSwitch);
        neighbours.remove(dstSwitch);
        this.neighborPortMap.remove(link.getSrcPort());
        return true;
    }

    /**
     * Adds switch to topology.
     *
     * @param sw the switch
     */
    protected void addSwitch(final T1 sw) {
        if (this.switchSet.add(sw)) {
            this.dpidMap.put(sw.getSwitchId(), sw);
            this.neighborMap.put(sw, new HashSet<T1>());
        }
    }

    /**
     * Removes switch from topology.
     *
     * @param sw the switch
     * @return true if successful, false otherwise
     */
    protected boolean removeSwitch(final T1 sw) {
        if (this.switchSet.remove(sw)) {
            this.neighborMap.remove(sw);
            this.dpidMap.remove(((Switch) sw).getSwitchId());
            return true;
        }
        return false;
    }

    // Public methods to query topology information

    /**
     * Returns neighbor switches of given switch.
     *
     * @param sw the switch
     * @return Unmodifiable set of switch instances.
     */
    public Set<T1> getNeighbors(final T1 sw) {
        return Collections.unmodifiableSet(this.neighborMap.get(sw));
    }

    /**
     * Returns neighbor port of given port.
     *
     * @param port the port
     * @return the neighbour port
     */
    public T2 getNeighborPort(final T2 port) {
        return this.neighborPortMap.get(port);
    }

    /**
     * Returns switch instance based on its dpid.
     *
     * @param dpid the datapath ID
     * @return the switch instance
     */
    public T1 getSwitch(final Long dpid) throws InvalidDPIDException {
        try {
            return this.dpidMap.get(dpid);
        } catch (ClassCastException | NullPointerException ex) {
            throw new InvalidDPIDException("DPID "
                                                   + HexString.toHexString(dpid) + " is unknown ");
        }
    }

    /**
     * Returns the unmodifiable set of switches belonging to the network.
     *
     * @return set of switches
     */
    public Set<T1> getSwitches() {
        return Collections.unmodifiableSet(this.switchSet);
    }

    /**
     * Returns the unmodifiable set of links belonging to the network.
     *
     * @return set of links
     */
    public Set<T3> getLinks() {
        return Collections.unmodifiableSet(this.linkSet);
    }

    /**
     * Gets the link instance between the given ports.
     *
     * @param srcPort the source port
     * @param dstPort the destination port
     * @return the link instance, null if it doesn't exist
     */
    public T3 getLink(final T2 srcPort, final T2 dstPort) {
        // TODO: optimize this because we iterate over all links
        for (final T3 link : this.linkSet) {
            if (link.getSrcPort().equals(srcPort)
                    && link.getDstPort().equals(dstPort)) {
                return link;
            }
        }
        return null;
    }

    /**
     * Boots this network.
     *
     * @return true if successful, false otherwise
     */
    public abstract boolean boot();

}
