package org.framework.algorithm.openStack;

import org.domain.*;
import org.framework.*;
import org.framework.reconfigurationAlgorithm.acoAlgorithm.AcoCall;
import org.framework.reconfigurationAlgorithm.concurrent.StaticReconfMemeCall;
import org.framework.reconfigurationAlgorithm.memeticAlgorithm.MASettings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.domain.VirtualMachine.getById;

/**
 * <b>Algorithm 4: OpenStack (Openstack)</b>
 *<p>
 *     No VMPr triggering nor recovering. 
 *</p>
 * @author Marcelo Velazquez
 * @since 06/08/18.
 */
public class Openstack {

		private static Logger logger = DynamicVMP.getLogger();
		
		private Openstack() {
        // Default Constructor
		}
		
		/**
     * OpenStack Manager
     * @param workload                   Workload Trace
     * @param physicalMachines           List of Physical Machines
     * @param virtualMachines            List of Virtual Machines
     * @param derivedVMs                 List of Derived Virtual Machines
     * @param revenueByTime              Revenue by time
     * @param wastedResources            WastedResources by time
     * @param wastedResourcesRatioByTime WastedResourcesRatio per time
     * @param powerByTime                Power Consumption by time
     * @param placements                 List of Placement by time
     * @param code                       Heuristics Algorithm Code
     * @param timeUnit                   Time init
     * @param requestsProcess            Type of Process
     * @param maxPower                   Maximum Power Consumption
     * @param scenarioFile               Name of Scenario
     *
     * <b>RequestsProcess</b>:
     *  <ul>
     *      <li>Requests[0]: requestServed Number of requests served</li>
     *      <li>Requests[1]: requestRejected Number of requests rejected</li>
     *      <li>Requests[2]: requestUpdated Number of requests updated</li>
     *      <li>Requests[3]: violation Number of violation</li>
     *  </ul>
     *
     * @throws IOException          Error managing files
     * @throws InterruptedException Multi-thread error
     * @throws ExecutionException   Multi-thread error
     */
	 
	 public static void openstackManager(List<Scenario> workload, List<PhysicalMachine> physicalMachines,
            List<VirtualMachine>
            virtualMachines, List<VirtualMachine> derivedVMs,
            Map<Integer, Float> revenueByTime, List<Resources> wastedResources,  Map<Integer, Float> wastedResourcesRatioByTime,
            Map<Integer, Float> powerByTime, Map<Integer, Placement> placements, Integer code, Integer timeUnit,
            Integer[] requestsProcess, Float maxPower, String scenarioFile)
            throws IOException, InterruptedException, ExecutionException 
		{
			List<APrioriValue> aPrioriValuesList = new ArrayList<>();
			List<VirtualMachine> vmsToMigrate = new ArrayList<>();
			List<Integer> vmsMigrationEndTimes = new ArrayList<>();
			ExecutorService executorService = Executors.newSingleThreadExecutor();
			MASettings memeConfig = Utils.getMemeConfig(true);
			Callable<Placement> staticReconfgTask;
			Future<Placement> reconfgResult = null;
			Placement reconfgPlacementResult = null;

			Boolean isMigrationActive = false;
			Boolean isUpdateVmUtilization = false;
			Integer actualTimeUnit;
			Integer nextTimeUnit;

			Integer memeticTimeInit = timeUnit + memeConfig.getExecutionFirstTime();
			Integer memeticTimeEnd=-1;

			Integer migrationTimeInit =- 1;
			Integer migrationTimeEnd =- 1;

			Integer vmEndTimeMigration = 0;
			
			for (int iterator = 0; iterator < workload.size(); ++iterator) {
			
				Scenario request = workload.get(iterator);
				actualTimeUnit = request.getTime();
				//check if is the last request, assign -1 to nextTimeUnit if so.
				nextTimeUnit = iterator + 1 == workload.size() ? -1 : workload.get(iterator + 1).getTime();
				
				if (nextTimeUnit!= -1 && isMigrationActive && DynamicVMP.isVmBeingMigrated(request.getVirtualMachineID(),
                    vmsToMigrate))
				{
					VirtualMachine vmMigrating = getById(request.getVirtualMachineID(),virtualMachines);

					vmEndTimeMigration = Utils.updateVmEndTimeMigration(vmsToMigrate, vmsMigrationEndTimes,
							vmEndTimeMigration,
							vmMigrating);

					isUpdateVmUtilization = actualTimeUnit <= vmEndTimeMigration;	
				
		
				}
				
				DynamicVMP.runHeuristics(request, code, physicalMachines, virtualMachines, derivedVMs, requestsProcess,
                    isUpdateVmUtilization);

				// check if its the last request or a variation of time unit will occurs.
				if (nextTimeUnit == -1 || !actualTimeUnit.equals(nextTimeUnit)) 
				{
					ObjectivesFunctions.getObjectiveFunctionsByTime(physicalMachines,
							virtualMachines, derivedVMs, wastedResources,
							wastedResourcesRatioByTime, powerByTime, revenueByTime, timeUnit, actualTimeUnit);

					Float placementScore = ObjectivesFunctions.getDistanceOrigenByTime(request.getTime(),
							maxPower, powerByTime, revenueByTime, wastedResourcesRatioByTime);

					// Print the Placement Score by Time t
					Utils.printToFile( Utils.OUTPUT + Utils.PLACEMENT_SCORE_BY_TIME + scenarioFile + Constant.EXPERIMENTS_PARAMETERS_TO_OUTPUT_NAME, placementScore);

					timeUnit = actualTimeUnit;

					Placement heuristicPlacement = new Placement(PhysicalMachine.clonePMsList(physicalMachines),
							VirtualMachine.cloneVMsList(virtualMachines),
							VirtualMachine.cloneVMsList(derivedVMs), placementScore);
					placements.put(actualTimeUnit, heuristicPlacement);
				}
				
				
				
					// Set Migration Active
                    isMigrationActive = false;				
				   
		
			}
			
			Utils.executorServiceTermination(executorService);
		}
		
		
	}