
package NR;

import static NR.Common.*;

public class indexx {

    
public static void  indexx(double [] arr,  int [] indx) throws NRException
{
     final int M = 7, NSTACK = 50;
     int i, indxt, ir, j, k, jstack = -1, l = 0;
     double  a;
     int [] istack = new int[NSTACK];

    int n = arr.length;
    ir = n-1;
    for (j=0; j<n; j++) indx[j] = j;
    for (;;) {
        if (ir-l < M) {
            for (j=l+1;j<=ir;j++) {
	indxt = indx[j];
    	a=arr[indxt];
	for (i=j-1;i>=l;i--) {
                    if (arr[indx[i]] <= a) break;
                    indx[i+1]=indx[i];
	}
	indx[i+1]=indxt;
	}
	if (jstack < 0) break;
	ir=istack[jstack--];
	l=istack[jstack--];
	} else {
	k=(l+ir) >> 1;
	swap(indx, k, l+1);
	if (arr[indx[l]] > arr[indx[ir]]) {
	swap(indx, l, ir);
                }
	if (arr[indx[l+1]] > arr[indx[ir]]) {
	 swap(indx, l+1, ir);
	}
	if (arr[indx[l]] > arr[indx[l+1]]) {
	 swap(indx, l, l+1);
	}
            i=l+1;
            j=ir;
            indxt=indx[l+1];
            a=arr[indxt];
        for (;;) {
            do i++; while (arr[indx[i]] < a);
            do j--; while (arr[indx[j]] > a);
            if (j < i) break;
            swap(indx, i, j);
            }
    indx[l+1]=indx[j];
    indx[j]=indxt;
    jstack += 2;
    if (jstack >= NSTACK) throw new NRException("NSTACK too small in indexx.");
    if (ir-i+1 >= j-l) {
        istack[jstack] = ir;
        istack[jstack-1]=i;
        ir=j-1;
    } else {
    istack[jstack]=j-1;
    istack[jstack-1]=l;
    l=i;
      }
     }
    }
}

static  public void  indexx(int []  arr, int []  indx) throws NRException
{
    final  int M=7, NSTACK=50;
    int i, indxt, ir, j, k, jstack=-1, l=0;
    int a;
    int []  istack = new int[NSTACK];
    int n=arr.length;
    ir=n-1;
    for (j=0;j<n;j++) indx[j]=j;
        for (;;) {
            if (ir-l < M) {
	for (j=l+1;j<=ir;j++) {
                    indxt=indx[j];
                    a=arr[indxt];
                    for (i=j-1;i>=l;i--) {
                        if (arr[indx[i]] <= a) break;
                            indx[i+1]=indx[i];
	}
	indx[i+1]=indxt;
                }
	if (jstack < 0) break;
	ir=istack[jstack--];
	l=istack[jstack--];
	} else {
	k=(l+ir) >> 1;
	swap(indx, k, l+1);
	if (arr[indx[l]] > arr[indx[ir]]) {
                    swap(indx, l, ir);
	}
	if (arr[indx[l+1]] > arr[indx[ir]]) {
	 swap(indx, l+1, ir);
	}
                if (arr[indx[l]] > arr[indx[l+1]]) {
	swap(indx, l,  l+1);
	}
                i=l+1;
                j=ir;
                indxt=indx[l+1];
                a=arr[indxt];
                for (;;) {
	do i++; while (arr[indx[i]] < a);
	do j--; while (arr[indx[j]] > a);
                  if (j < i) break;
	swap(indx, i, j);
	}
	indx[l+1]=indx[j];
	indx[j]=indxt;
	jstack += 2;
	if (jstack >= NSTACK) throw new NRException("NSTACK too small in indexx.");
	if (ir-i+1 >= j-l) {
                    istack[jstack]=ir;
                    istack[jstack-1]=i;
                    ir=j-1;
	} else {
                    istack[jstack]=j-1;
                    istack[jstack-1]=l;
                    l=i;
	}
        }
    }
}

}
