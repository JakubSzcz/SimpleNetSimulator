package Protocols;

public class IPv4 {
    /////////////////////////////////////////////////////////
    //                     functions                       //
    /////////////////////////////////////////////////////////

    //return true if given address is correct
    public static boolean is_valid(long ip_address){

        return true;
    }

    //parse ip address from String to long
    public static long parse_to_long(String ip_address_string){

        //split ip_address_string into String ip_octets
        String[] ip_octets_string = ip_address_string.split("\\.");

        //parse octets(str) to long_octets and shift each of them in left, eventually sum up to ip_address_long
        long[] ip_octets_long = new long[4];
        long ip_address_long = 0;
        for(int i = 0; i < 4; i++){
            ip_octets_long[i] = Integer.parseInt(ip_octets_string[i]);
            ip_octets_long[i] = ip_octets_long[i] << (24-8*i);
            ip_address_long += ip_octets_long[i];
        }

        return ip_address_long;
    }

    //parse ip address from long to String
    public static String parse_to_string(long ip_address_long){

        return "nic";
    }

}
