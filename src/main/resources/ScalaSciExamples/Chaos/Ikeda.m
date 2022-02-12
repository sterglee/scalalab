clear all;
R = 1;  C1 = 0.4;  C2 = 0.9;  C3 = 6;
tic;
N=2500000;

x(1)=0.12; y(1) = 0.2;
k = 2; km = 0;
tau=0.0; sintau=0.0;  costau=0.0;

while k < (N+1),
  km=k-1;
  tau = C1-C3/(1+x(km)*x(km)+y(km)*y(km));
  sintau = sin(tau); costau = cos(tau);
  x(k) = R+C2*(x(km)*costau-y(km)*sintau);
  y(k) = C2*(x(km)*sintau+y(km)*costau);
  k=k+1;
end

tm = toc
plot(x, y, '.');