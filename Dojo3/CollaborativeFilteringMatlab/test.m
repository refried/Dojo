

D = dlmread('../CollaborativeFiltering/u.data', '\t');

maxUser = max(D(:, 1))
maxItem = max(D(:, 2))

R = zeros(maxUser, maxItem);

% Build a matrix of values from the file
for r = 1:size(D, 1)
    R(D(r,1), D(r,2)) = D(r,3);
end

P = neighborhoodbased(R);

% MAE - note...only add up the ones in which the original is 0
diff = P(R ~= 0) - R(R ~= 0);
sum(abs(diff)) / nnz(R)
