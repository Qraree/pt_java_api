package org.core.events.listeners;

import com.cisco.pt.ipc.events.LogicalWorkspaceEvent;
import com.cisco.pt.ipc.sim.Device;
import java.util.UUID;
import javafx.application.Platform;
import org.core.DeviceManager;
import org.core.events.EventListener;
import org.core.operations.OperationState;

public class LogicalWorkspaceEventListener implements EventListener<LogicalWorkspaceEvent> {

  DeviceManager deviceManager;

  public LogicalWorkspaceEventListener(DeviceManager deviceManager) {
    this.deviceManager = deviceManager;
  }

  @Override
  public void handleEvent(LogicalWorkspaceEvent event) {
    System.out.println(event.type);
    switch (event.type) {
      case DEVICE_ADDED:
        LogicalWorkspaceEvent.DeviceAdded LWEvent = (LogicalWorkspaceEvent.DeviceAdded) event;

        Device device = deviceManager.getDeviceByName(LWEvent.name);
        //        OperationState.getInstance().pushDevice(device);

        Platform.runLater(() -> OperationState.getInstance().pushGUIDevice(device));

        UUID currentOperationUUID = OperationState.getInstance().getCurrentOperationUUID();

        if (currentOperationUUID == null) {
          System.out.println("No operation found");
          return;
        }

        break;
      case DEVICE_REMOVED:
        assert event instanceof LogicalWorkspaceEvent.DeviceRemoved;
        LogicalWorkspaceEvent.DeviceRemoved devRemovedEvent =
            (LogicalWorkspaceEvent.DeviceRemoved) event;

        Platform.runLater(
            () -> OperationState.getInstance().removeFromGUIDevices(devRemovedEvent.name));

        break;
      case LINK_CREATED:
        assert event instanceof LogicalWorkspaceEvent.LinkCreated;
        LogicalWorkspaceEvent.LinkCreated linkCreateEvent =
            (LogicalWorkspaceEvent.LinkCreated) event;

        System.out.println("First device: " + linkCreateEvent.deviceName1);
        System.out.println("First device port: " + linkCreateEvent.portName1);

        System.out.println("Second device: " + linkCreateEvent.deviceName2);
        System.out.println("Second device port: " + linkCreateEvent.portName2);

        break;
    }
  }
}
