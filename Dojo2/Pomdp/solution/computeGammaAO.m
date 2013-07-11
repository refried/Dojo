function [GammaAO,GammaAOS,GammaAOnew] = computeGammaAO(V0)
% computeGammaAO - compute the backprojected vectors from t+1
%
% [GammaAO,GammaAOS] = computeGammaAO(V0)
%
% V0 - struct array of alpha vectors
%
% GammaAO  - size(V0){nrA}{nrO} backprojected copies from V0
% GammaAOS - sparse(GammaAO)

% Author: Matthijs Spaan
% $Id: computeGammaAO.m,v 1.7 2004/07/14 14:51:15 mtjspaan Exp $
% Copyright (c) 2003,2004 Universiteit van Amsterdam.  All rights reserved.
% More information is in the file named COPYING.

global problem;

nrA=problem.nrActions;
nrO=problem.nrObservations;
nrS=problem.nrStates;

[nrInV,foo]=size(V0);
V0v=vertcat(V0.v);

gamma=problem.gamma;

% Step 1
for a=1:nrA
  observation=problem.observation;
  for o=1:nrO
      GammaAO{a}{o}=zeros(nrInV,nrS);
      for k=1:nrInV
        for s1=1:nrS
          GammaAO{a}{o}(k,:)=GammaAO{a}{o}(k,:) + ...
              gamma*transition(s1,:,a)* ...
              observation(s1,a,o)*V0v(k,s1);
        end
      end
  end
end
