kernel = 1;
switch kernel
    case 1; k = @(x, y) 1*x'*y; % Linear
    case 2; k = @(x, y) 1*min(x, y); % Brownian
    case 3; k = @(x, y) exp(-100*(x-y)'*(x-y)); % squared exponential
    case 4; k = @(x, y) exp(-1*sqrt((x-y)'*(x-y))); % Ornstein-Uhlenbeck
    case 5; k = @(x, y) exp(-1*sin(5*pi*(x-y))^2); % periodic
    case 6; k = @(x, y) exp(-100*min(abs(x-y),abs(x+y))^2); % symmetric along the Y axis
end

% Choose points at which to sample
x = (0:.05:1);
n = length(x);

% Construct the covariance matrix
C = zeros(n, n);
for i = 1:n
    for j = 1:n
        C(i, j) = k(x(i), x(j));
    end
end

% Sample from Gaussian process
u = randn(n, 1);
[A,S,B] = svd(C);
z = A*sqrt(S)*u;

% Plot
figure(2); hold on; clf;
plot(x, z, '.')
axis([0,1,-2,2])