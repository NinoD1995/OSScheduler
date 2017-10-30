import java.io.*;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;

class Scheduler {
    static int numOfProcesses;
    static int preemptive;
    static int quantum;
    static Process[] processes;

    public static void main(String[] args) throws IOException {
        processes = getProcesses();
        Dispatcher.roundRobin(processes);
        System.out.println("Scheduling Completed!");
    }

    public static Process[] getProcesses() throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("input.data"));
        Queue<Process> queue = new LinkedList<>();
        int arrival;
        int burst;
        int priority;
        int i=0;

        numOfProcesses = scanner.nextInt();
        

        processes = new Process[numOfProcesses];

        preemptive = scanner.nextInt();
        if(preemptive == 1)
            System.out.println("Using preemptive/Round Robin scheduling...");

        quantum = scanner.nextInt();
        System.out.println("The Time Quantum is " + quantum);
        System.out.println(numOfProcesses + " processes");

        while(scanner.hasNextInt()) {                               //read data of processes
            arrival = scanner.nextInt();
            burst = scanner.nextInt();
            priority = scanner.nextInt();
            System.out.println(arrival + " " + burst + " " + priority + " ");
            processes[i] = new Process(arrival, burst, priority);   //puts each processes in array
            i++;
        }
        scanner.close();
        return processes;
    }
}

class Dispatcher {
    static Queue<Process> queue = new LinkedList<>();

    private static void orderByArrival(Process[] processes) {
        Process lowestArrival;
        Process placeholder;

        for(int i=0; i<processes.length; i++) {                     //sorts processes by arrival time
            int index = i;
            for(int j = i+1; j < processes.length; j++) {
                if (processes[j].arrival < processes[index].arrival)
                    index = j;

                Process smaller = processes[index];
                processes[index] = processes[i];
                processes[i] = smaller;
            }
        }
        for(int i=0; i<processes.length; i++){
            processes[i].name = i+1;                                //gives each process a name starting with 1
            queue.add(processes[i]);                                //adds each process to queue
        }
    }

    public static void roundRobin(Process[] processes) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.data"));
        int quantum = Scheduler.quantum;
        int startTime=0;
        int endTime;
        Process currentProcess;
        int i=0;

        orderByArrival(processes);

        while(!queue.isEmpty()) {                                   //checks if all processes are done
            currentProcess = queue.remove();
            currentProcess.burst = currentProcess.burst - quantum;  //gets remaining burst time
            endTime = startTime + quantum;
            writer.write(startTime + " " + endTime + " " + currentProcess.name + "\n");
            startTime = endTime;

            if(currentProcess.burst > 0)                            //returns process to queue if not completed
                queue.add(currentProcess);
            i++;
        }
        writer.close();
        
    }
}

class Process  {
    int arrival;
    int burst;
    int priority;
    int name;

    public Process (int initArrival, int initBurst, int initPriority){
        arrival = initArrival;
        burst = initBurst;
        priority = initPriority;
    }
}