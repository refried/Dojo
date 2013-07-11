function initProblem
% Problem specific initialization function for Tag.
% $Id: initProblem.m,v 1.8 2004/09/20 15:04:08 mtjspaan Exp $

clear global pomdp;
global problem;
global pomdp;

% String describing the problem.
problem.description='simple tiger';

% String used for creating filenames etc.
problem.unixName='tiger';

% Use sparse matrix computation.
problem.useSparse=0;

% Load the (cached) .POMDP, defaults to unixName.POMDP.
initPOMDP('tiger.aaai.POMDP');

% Generic POMDP initialization code. Should be called after initPOMDP.
initProblemGeneric;

% This allows us to use the default episodeEnded.m.
problem.goodReward=max(problem.rewards);
