clear all, close all;

meanfunc = {@meanZero}; hyp.mean = [];
%covfunc = {@covLIN}; hyp.cov = [];
covfunc = {@covPoly,3}; c=2; sf=2; hyp.cov = log([c;sf]);
likfunc = @likGauss; sn=0.1; hyp.lik = log(sn);

x = (-1:.5:1)';
K = feval(covfunc{:}, hyp.cov, x);
mu = feval(meanfunc{:}, hyp.mean, x);
y = -4*x.*x+4;

z = linspace(-1.0, 1.0, 11)';
[m s2] = gp(hyp, @infExact, meanfunc, covfunc, likfunc, x, y, z);

figure(1); hold on; clf;
f = [m+2*sqrt(s2); flipdim(m-2*sqrt(s2), 1)];
fill([z; flipdim(z, 1)], f, [7 7 7]/8)
hold on; plot(z, m); plot(x, y, '+');

