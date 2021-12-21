package Devices;

public class Link extends Thread{
    /////////////////////////////////////////////////////////
    //                 variables and objects               //
    /////////////////////////////////////////////////////////

    // ends of link
    private NetworkInterface end_1;
    private NetworkInterface end_2;

    // delay in ms
    final private int delay = 200;

    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    // constructor
    Link(NetworkInterface end_1, NetworkInterface end_2){
        this.end_1 = end_1;
        this.end_2 = end_2;
        start();
    }

    public void run(){
        while (true){
            if (!end_1.is_out_empty()){
                end_2.handle_frame(end_1.get_frame());
            }
            if (!end_2.is_out_empty()){
                end_1.handle_frame(end_2.get_frame());
            }

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
