package org.ndsc.mimicIO.elements.link;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.ndsc.mimicIO.elements.Persistable;
import org.ndsc.mimicIO.elements.datapath.PhysicalSwitch;
import org.ndsc.mimicIO.elements.port.PhysicalPort;

import java.util.Map;

/**
 * Created by cgz on 12/27/16.
 */
public class PhysicalLink extends Link<PhysicalPort, PhysicalSwitch> implements
        Persistable, Comparable<PhysicalLink> {

    @SerializedName("linkId")
    @Expose
    private Integer linkId = null;

    /**
     * Instantiates a new physical link.
     *
     * @param srcPort
     *            the source port
     * @param dstPort
     *            the destination port
     */
    public PhysicalLink(final PhysicalPort srcPort, final PhysicalPort dstPort) {
        super(srcPort, dstPort);
        srcPort.setOutLink(this);
        dstPort.setInLink(this);
        this.linkId = PhysicalLink.linkIds.getAndIncrement();
    }

    public Integer getLinkId() {
        return linkId;
    }

    @Override
    public void unregister() {
        this.getSrcSwitch().getMap().removePhysicalLink(this);
        srcPort.setOutLink(null);
        dstPort.setInLink(null);
    }

    @Override
    public Map<String, Object> getDBObject() {
        Map<String, Object> dbObject = super.getDBObject();
        dbObject.put(TenantHandler.LINK, this.linkId);
        return dbObject;
    }

    public void setLinkId(Integer id) {
        this.linkId = id;
    }

    @Override
    public int compareTo(PhysicalLink o) {
        Long sum1 = this.getSrcSwitch().getSwitchId()
                + this.getSrcPort().getPortNumber();
        Long sum2 = o.getSrcSwitch().getSwitchId()
                + o.getSrcPort().getPortNumber();
        if (sum1 == sum2) {
            return (int) (this.getSrcSwitch().getSwitchId() - o.getSrcSwitch()
                    .getSwitchId());
        } else {
            return (int) (sum1 - sum2);
        }
    }

}
