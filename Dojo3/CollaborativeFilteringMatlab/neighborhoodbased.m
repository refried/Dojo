%
% neighborhoodbased.m - Neighborhood based collaborative filtering.
%
% Given a (users x items) matrix of user ratings, compute a matrix that
% gives a predicted rating for every user and item.  This is implemented
% using neighborhood based filtering, which is based on the pearson
% coefficient of correlation.
%
function [P] = neighborhoodbased(R)

    numUser = size(R, 1)
    numItem = size(R, 2)
    w       = zeros(numUser, numUser);
    means   = zeros(numUser, 1);

    Rt = R';

    % Compute the user -> user weights, and the user means
    % w(u,v) = (sum u in U, v in V: (Rui - Rubar(v))(Rvi - Rvbar(u))) /
    %          (sqrt((Rui - Rubar(v))^2) * sqrt((Rvi - Rvbar(u))^2)
    for i = 1:numUser
        Dt = Rt(~any(Rt(:, i) == 0, 2), :);
        means(i) = mean(Dt(:, i));
        for j = i:numUser
            flt = ~any(Dt(:, j) == 0, 2);
            Vu = Dt(flt, i);
            Vv = Dt(flt, j);
            sizeV = size(Vu, 1);
            if (sizeV >= 2)
                Vud = Vu - (sum(Vu) / sizeV);
                Vvd = Vv - (sum(Vv) / sizeV);
                num = sum(Vud .* Vvd);
                denom = sqrt(sum(Vud .^ 2)) * sqrt(sum(Vvd .^ 2));
                if denom ~= 0
                    val = num / denom;
                    w(i, j) = val;
                    w(j, i) = val;
                else
                    w(i, j) = 0;
                    w(j, i) = 0;
                end
            end
        end
        %w(i, i) = 0;
    end

    % Compute the prediction matrix p for every user and every item
    
    % Only subtract non-zero elements, to avoid (0 - u) situations.
    function [a] = minus_if_non_zero(Ri, x)
        nz = ~any(Ri == 0, 2);
        a = Ri;
        a(nz, :) = a(nz, :) - x(nz, :);

    end

    % P_ai = abar + (sum u in U: (r_ui - r_ubar) * w_au)/sum u in U: |w_au|
    diff = bsxfun(@minus_if_non_zero, R, means);

    P = zeros(numUser, numItem);
    for j = 1:numItem
        nz = ~any(R(:, j) == 0, 2);
        denom = sum(abs(w(:, nz)), 2);
        delta = 0;
        if denom ~= 0
            delta = (w(:, nz) * diff(nz, j)) ./ denom;
        end
        P(:, j) = means + delta;
    end
end