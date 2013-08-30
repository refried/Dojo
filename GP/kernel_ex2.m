kernel = 2;
switch kernel
    case 1; k = @(x, y) 1*x'*y; % Linear
    case 2; k = @(x, y) exp(-100*(x-y)'*(x-y)); % squared exponential
end

% Choose points at which to sample
points = (0:.05:1)';
[U V] = meshgrid(points, points);
x = [U(:) V(:)]';
n = size(x,2);

% Construct the covariance matrix
C = zeros(n, n);
for i = 1:n
    for j = 1:n
        C(i, j) = k(x(:,i), x(:,j));
    end
end

% Sample from Gaussian process
u = randn(n, 1);
[A,S,B] = svd(C);
z = A*sqrt(S)*u;

% Plot
figure(2); hold on; clf;
Z = reshape(z,sqrt(n),sqrt(n));
surf(U,V,Z);