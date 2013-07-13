function [P] = neighborhoodbased(R)

    numUser = size(R, 1)
    numItem = size(R, 2)
    w       = zeros(numUser, numUser);
    means   = zeros(numUser, 1);

    Rt = R';

    % Compute the user -> user weights, and the user means
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

    
    % THIS CODE WORKS AND IS GOOD...ITS JUST SLOW
    
    %     P = zeros(numUser, numItem);
%     for i = 1:numUser
%         for j = 1:numItem
%             nz = ~any(R(:, j) == 0, 2);
%             denom = sum(abs(w(i, nz)));
%             delta = 0;
%             if denom ~= 0
%                 delta = (w(i, nz) * diff(nz, j)) / denom;
%             end
%             P(i, j) = means(i) + delta;
%         end
%     end

    function [a] = minus_if_non_zero(Ri, x)
        nz = ~any(Ri == 0, 2);
        a = Ri;
        a(nz, :) = a(nz, :) - x(nz, :);

    end
    % P_ai = abar + (sum u in U: (r_ui - r_ubar) * w_au)/sum u in U: |w_au|
    diff = bsxfun(@minus_if_non_zero, R, means);
    wdiff = (diff' * w)';

    % TODO: This isnt right...this should not include cases filtered out
    % because the data was not in the original matrix.  See above
    denom = sum(abs(w));
    %delta = wdiff ./ repmat(denom, size(wdiff, 1), 1);
    delta = bsxfun(@rdivide, wdiff, denom');
    
    A = repmat(means, 1, numItem);
    P = A + delta;
    
    %      for i = 1:numUser
%          Dt = Rt(~any(Rt(:, i) == 0, 2), :);
%         
%          for j = i:numUser
%              flt = ~any(Dt(:, j) == 0, 2);
%              V = [Dt(flt, i) Dt(flt, j)];
%              sizeV = size(V, 1);
%              if (sizeV >= 2)
%                  rbar = sum(V) / sizeV;
%                  Vdiff = bsxfun(@minus, V, rbar);
%                  num = sum(prod(Vdiff, 2));
%                  denom = prod(sqrt(sum(Vdiff .^ 2)));
%                  if denom ~= 0
%                      w(i, j) = num / denom;
%                      w(j, i) = num / denom;
%                  else
%                      w(i, j) = 0;
%                      w(j, i) = 0;
%                  end
%              end
%          end
%      %   i
%      end
     
     %    

%      
%           for i = 1:numUser
%          D = R(:, ~any(R(i, :) == 0, 1));
%         
%          for j = i:numUser
%              flt = ~any(D(j, :) == 0, 1);
%              V = [D(i, flt) ; D(j, flt)];
%              sizeV = size(V, 2);
%              if (sizeV >= 2)
%                  rbar = sum(V, 2) / sizeV;
%                  Vdiff = bsxfun(@minus, V, rbar);
%                  num = sum(prod(Vdiff));
%                  denom = prod(sqrt(sum(Vdiff .^ 2, 2)));
%                  if denom ~= 0
%                      w(i, j) = num / denom;
%                      w(j, i) = num / denom;
%                  else
%                      w(i, j) = 0;
%                      w(j, i) = 0;
%                  end
%              end
%          end
%      %   i
%      end
%      
end