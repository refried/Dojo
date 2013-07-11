function graphVF()
%GRAPHVF Summary of this function goes here
%   Detailed explanation goes here

global vi

clf;
hold all;
for i=1:size(vi.V, 1)
    plot([0 1], vi.V(i).v);
end
axis([0 1 -1 20]);