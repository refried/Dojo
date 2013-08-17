%
% test.m - Test the collaborative filtering implementations with supplied
% data.
%

D = dlmread('../CollaborativeFiltering/u.data', '\t');

maxUser = max(D(:, 1))
maxItem = max(D(:, 2))

R = zeros(maxUser, maxItem);

% Build a matrix of values from the file
for r = 1:size(D, 1)
    R(D(r,1), D(r,2)) = D(r,3);
end

% Predict using neighborhood based filtering.
P = neighborhoodbased(R);

% MAE - note...only compare items that were non-zero in R (e.g. our
% "known" data)
diff = P(R ~= 0) - R(R ~= 0);

% Compute and print out the MAE
sum(abs(diff)) / nnz(R)
