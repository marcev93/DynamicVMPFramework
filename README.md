To develop the framework:
1. Maven 3 or greater
2. Java 8 (JDK 1.8)
3. Java IDE (recommended Netbeans)

How to install Maven on Windows:
To install Apache Maven on Windows, you just need to
download the Maven’s zip file, and Unzip it to the 
directory you wish to install, and configure the 
Windows environment variables.

See more: https://www.mkyong.com/maven/how-to-install-maven-in-windows/

How to install Maven on Linux:

Apache Maven depends on Java Development Kit so you 
must have either Oracle JDK or OpenJDK installed 
on your system.

See more: https://www.javahelps.com/2017/10/install-apache-maven-on-linux.html

Before running the project make sure the input 
folder is populated with the scenarios files 
on .csv format. 

Steps:
1. To compile the project, go to the root folder 
of the repository donwloaded and run the following 
command on a terminal or PowerShell Windows:

mvn clean package 

2. After that, run the project using the following command:
java -jar target/DynamicVMPFramework.jar inputs/<parameters>

Where <parameters> represents the name of the parameters 
file you created. 

The <parameters> file must have the following structure:

APPROACH = Algorithm approach
CENTRALIZED
DISTRIBUTED -> This approach will automatically launch the distributed approach and you don't need to specify the following inputs: VMPr, VMPr_TRIGGERING, VMPr_RECOVERING.

iVMP = Algorithm for the incremental phase (iVMP).
FF  -> First Fit
BF  -> Best Fit
WF  -> Worst Fit
FFD -> First Fit Decreasing
BFD -> Best Fit Decreasing
OS  -> OpenStack

VMPr = Algorithm for the reconfiguration phase (VMPr).
MEMETIC -> Memetic Algorithm
ACO -> Ant Colony Optimization

VMPr_TRIGGERING = VMPr triggering strategy
PERIODICALLY
PREDICTION-BASED

VMPr_RECOVERING = VMPr recovering strategy
CANCELLATION
UPDATE-BASED

PM_CONFIG = Load CPU Configuration
LOW -> (<10%)
MED -> (<30%)
HIGH -> (<80%)
FULL -> (<95%)
SATURATED -> (<120%)

DERIVE_COST = Cost per each derived VM

PROTECTION_FACTOR_01 = Resource1 protection factor [0;1]
PROTECTION_FACTOR_02 = Resource2 protection factor [0;1]
PROTECTION_FACTOR_03 = Resource3 protection factor [0;1]

PENALTY_FACTOR_01 = Resource1 penalty factor (greater than 1)
PENALTY_FACTOR_02 = Resource1 penalty factor (greater than 1)
PENALTY_FACTOR_03 = Resource1 penalty factor (greater than 1)

INTERVAL_EXECUTION_MEMETIC = Periodic Time of MA Execution

POPULATION_SIZE = Population size for MA

NUMBER_GENERATIONS = Generations size for MA

EXECUTION_DURATION = Time of Duration

LINK_CAPACITY = Link Capacity for Migration

MIGRATION_FACTOR_LOAD = Factor Load per Migration

HISTORICAL_DATA_SIZE = Historical Data Sieze

FORECAST_SIZE = Forecast Size

SCALARIZATION_METHOD = Scalarization Method
ED -> Euclidean Distance
MD -> Manhattan Distance
CD -> Chevyshev Distance
WS -> Weighted Sum

MAX_PHEROMONE = Max pheromone allowed in ACO
PHEROMONE_CONSTANT = Pheromone constant for ACO, range [0,1], determines how fast pheromone evaporates. Pheromones evaporates quicker as pheromone constant grows
N_ANTS = Number of ants used for ACO
ACO_ITERATIONS = Number of iterations to be performed in ACO to return a solution

SCENARIOS = List of Request


3. Example of <parameters> file content:

APPROACH=CENTRALIZED

iVMP=FFD

VMPr=MEMETIC

VMPr_TRIGGERING=PREDICTION-BASED

VMPr_RECOVERING=UPDATE-BASED

PM_CONFIG=PM_CONFIG_LOW_LOAD

DERIVE_COST=0.7

PROTECTION_FACTOR_01=0.5

PROTECTION_FACTOR_02=0.5

PROTECTION_FACTOR_03=0.5

PENALTY_FACTOR_01=1

PENALTY_FACTOR_02=1

PENALTY_FACTOR_03=1

INTERVAL_EXECUTION_MEMETIC=10

POPULATION_SIZE=5

NUMBER_GENERATIONS=10

EXECUTION_DURATION=1

LINK_CAPACITY=50

MIGRATION_FACTOR_LOAD=10

HISTORICAL_DATA_SIZE=10

FORECAST_SIZE=5

SCALARIZATION_METHOD=ED

MAX_PHEROMONE=1
PHEROMONE_CONSTANT=0.1

N_ANTS=100

ACO_ITERATIONS=50

SCENARIOS

pppp-00.csv



4. Output Files

The framework generates the following files:

economical_penalties: Average economical penalties per each SLA violation.
economical_revenue: Average ecomical revenue per each VM hosted in the main provider.
leasing_costs: Average economical revenue lost per each VM hosted in an alternative provider from federation.
power_consumption: Average power energy consumed
reconfiguration_call_times: Number of reconfiguration calls.
wasted_resources: Average of wasted resources (one column per resource)
wasted_resources_ratio: Average of wasted resources (considering all resources)
scenarios_scores: Score per each executed scenario.





