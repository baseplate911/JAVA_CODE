public class bin{
    void bin_search(int low,int high,int arr[],int x)
    {
        int mid;
        mid=(low+high)/2;
        if(arr[mid]==x)
        {
            System.out.println("ELEMNT FOUND AT INDEX"+mid);
        }
        
            while(high>low)
            {
                if(x<arr[mid])
                {
                    low=0;
                    high=mid-1;
                }
                else
                {
                    low=mid+1;
                    high=10;//size of the array taken.
                }
            }
    }
public static void main( String[] args)
{

    int arr[]={1,2,3,4,5,6,7,8,9,10};
    bin obj=new bin();
    obj.bin_search(0,9,arr,8);
}
}
